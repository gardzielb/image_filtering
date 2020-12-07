package com.gardzielb.imgproc.filters


data class Pixel(val x: Int, val y: Int)


interface PixelCollection : Collection<Pixel>


class NullPixelCollection : PixelCollection {

	override val size = 0

	override fun contains(element: Pixel) = false

	override fun containsAll(elements: Collection<Pixel>) = false

	override fun isEmpty(): Boolean = true

	override fun iterator(): Iterator<Pixel> = listOf<Pixel>().iterator()
}
