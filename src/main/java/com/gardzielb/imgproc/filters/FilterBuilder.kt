package com.gardzielb.imgproc.filters

import com.gardzielb.imgproc.IntMatrix


// Handles creation of template filters using chain of responsibility pattern
// by providing hard-coded matrices for each supported filter type
abstract class MatrixFilterBuilder {

	private lateinit var next: MatrixFilterBuilder

	fun setNext(next: MatrixFilterBuilder): MatrixFilterBuilder {
		this.next = next
		return this.next
	}

	fun buildFilter(filterData: FilterData, offset: Int = 0): ImageFilter {
		val matrix = tryCreateMatrix(filterData.filterType)
		if (matrix != null)
			return MatrixImageFilter(filterData.img!!, matrix)
		return next.buildFilter(filterData, offset)
	}

	abstract fun tryCreateMatrix(filterType: FilterType): IntMatrix?
}


class IdentityMatrixFilterBuilder : MatrixFilterBuilder() {

	override fun tryCreateMatrix(filterType: FilterType): IntMatrix? {

		if (filterType != FilterType.IDENTITY)
			return null
		return IntMatrix(arrayOf(arrayOf(1)))
	}
}


class BlurMatrixFilterBuilder : MatrixFilterBuilder() {

	override fun tryCreateMatrix(filterType: FilterType): IntMatrix? {

		if (filterType != FilterType.BLUR)
			return null

		return IntMatrix(
				arrayOf(
						arrayOf(1, 1, 1, 1, 1),
						arrayOf(1, 1, 1, 1, 1),
						arrayOf(1, 1, 1, 1, 1),
						arrayOf(1, 1, 1, 1, 1),
						arrayOf(1, 1, 1, 1, 1)
				)
		)
	}
}


class SharpenMatrixFilterBuilder : MatrixFilterBuilder() {

	override fun tryCreateMatrix(filterType: FilterType): IntMatrix? {

		if (filterType != FilterType.SHARPEN)
			return null

		return IntMatrix(
				arrayOf(
						arrayOf(0, -1, 0),
						arrayOf(-1, 5, -1),
						arrayOf(0, -1, 0),
				)
		)
	}
}


class ReliefMatrixFilterBuilder : MatrixFilterBuilder() {

	override fun tryCreateMatrix(filterType: FilterType): IntMatrix? {

		if (filterType != FilterType.RELIEF)
			return null

		return IntMatrix(
				arrayOf(
						arrayOf(-1, -1, 0),
						arrayOf(-1, 1, 1),
						arrayOf(0, 1, 1),
				)
		)
	}
}


class EdgeDetectMatrixFilterBuilder : MatrixFilterBuilder() {

	override fun tryCreateMatrix(filterType: FilterType): IntMatrix? {

		if (filterType != FilterType.EDGE_DETECT)
			return null

		return IntMatrix(
				arrayOf(
						arrayOf(-1, -1, -1),
						arrayOf(-1, 8, -1),
						arrayOf(-1, -1, -1),
				)
		)
	}
}
