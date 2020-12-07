package com.gardzielb

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File

class App : Application() {

	override fun start(primaryStage: Stage?) {
		val loader = FXMLLoader(File("src/main/resources/main.fxml").toURI().toURL())
		val root = loader.load<Parent>()
		val scene = Scene(root)
		scene.stylesheets.add(File("src/main/resources/default.css").toURI().toURL().toExternalForm())

		primaryStage!!.scene = scene
		primaryStage.title = "Image processor"
		primaryStage.show()
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(App::class.java)
		}
	}
}