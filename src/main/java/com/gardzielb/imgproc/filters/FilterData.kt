package com.gardzielb.imgproc.filters

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.image.Image

data class FilterData(
		var filterType: FilterType,
		var filterRange: FilterRange,
		var img: Image? = null,
		val brushRadiusProperty: IntegerProperty = SimpleIntegerProperty()
)

enum class FilterType {
	IDENTITY, BLUR, SHARPEN, RELIEF, EDGE_DETECT, CUSTOM
}

enum class FilterRange {
	IMAGE, CIRCLE, POLYGON
}