package com.unltm.distance.base.collection

sealed class ExpansionType {
    object Image : ExpansionType()
    object Record : ExpansionType()
    object Location : ExpansionType()
    object Music : ExpansionType()
    object Video : ExpansionType()
    object Document : ExpansionType()
}
