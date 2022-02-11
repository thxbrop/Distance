package com.unltm.distance.components.layout

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.annotation.IntDef
import kotlin.math.abs

const val MOVE_DISTANCE_X = 20
const val MOVE_DISTANCE_Y = 20

class SensorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), SensorEventListener {
    private var accelerometerValues: FloatArray? = null
    private var magneticValues: FloatArray? = null
    private val sensorManager: SensorManager? =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
    private val scroller: Scroller = Scroller(context)

    @ADirection
    var direction = ADirection.DIRECTION_LEFT

    var degreeYMin = -40

    var degreeYMax = 40

    var degreeXMin = -40

    var degreeXMax = 40


    private val values = FloatArray(3)
    private val r = FloatArray(9)

    init {
        sensorManager?.let {
            val accelerometerSensor = it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val magneticSensor = it.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            it.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
            it.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = it.values
            }

            if (it.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = it.values
            }

            if (accelerometerValues != null && magneticValues != null) {
                SensorManager.getRotationMatrix(r, null, accelerometerValues, magneticValues)
            }
            SensorManager.getOrientation(r, values)

            val degreeX = Math.toDegrees(values[1].toDouble()).toFloat()
            val degreeY = Math.toDegrees(values[2].toDouble()).toFloat()

            var scrollX = scroller.finalX
            var scrollY = scroller.finalY

            if (degreeY <= 0 && degreeY > degreeYMin) {
                scrollX = (degreeY / abs(degreeYMin) * MOVE_DISTANCE_X * direction).toInt()
            } else if (degreeY > 0 && degreeY > degreeYMax) {
                scrollX = (degreeY / abs(degreeYMax) * MOVE_DISTANCE_X * direction).toInt()
            }
            if (degreeX <= 0 && degreeX > degreeXMin) {
                scrollY = (degreeX / abs(degreeXMin) * MOVE_DISTANCE_Y * direction).toInt()
            } else if (degreeX > 0 && degreeX > degreeXMax) {
                scrollY = (degreeX / abs(degreeXMax) * MOVE_DISTANCE_Y * direction).toInt()
            }
            smoothScroll(scrollX, scrollY)
        }
    }

    private fun smoothScroll(destX: Int, destY: Int) {
        val scrollY = scrollY
        val delta = destY - scrollY
        scroller.startScroll(destX, scrollY, 0, delta, 200)
        invalidate()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    fun unregister() {
        sensorManager?.unregisterListener(this)
    }

    @IntDef(ADirection.DIRECTION_LEFT, ADirection.DIRECTION_RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    @Target(
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.CLASS,
        AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY
    )
    annotation class ADirection {
        companion object {
            const val DIRECTION_LEFT = 1
            const val DIRECTION_RIGHT = -1
        }
    }
}