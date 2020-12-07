package com.gardzielb.imgproc.histogram

import javafx.scene.image.Image

interface HistogramPresenter {

    fun updateHistogram(img: Image)
}