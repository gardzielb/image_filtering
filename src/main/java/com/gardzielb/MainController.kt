package com.gardzielb

import com.gardzielb.imgproc.*
import com.gardzielb.imgproc.filters.*
import com.gardzielb.imgproc.histogram.ChartHistogramPresenter
import com.gardzielb.imgproc.histogram.HistogramPresenter
import com.gardzielb.ui.state.FilterStateMachine
import com.gardzielb.ui.ImageController
import com.gardzielb.ui.state.NullFilteringState
import com.gardzielb.ui.state.CircleBrushFilterStateBuilder
import com.gardzielb.ui.state.EntireImageFilterStateBuilder
import com.gardzielb.ui.state.PolygonSelectionFilterStateBuilder
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Group
import javafx.scene.chart.AreaChart
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import java.io.File
import java.net.URL
import java.util.*
import kotlin.math.round

const val customMatrixRows = 3
const val customMatrixColumns = 3

// UI controller
class MainController : Initializable {

	private lateinit var imageController: ImageController
	private lateinit var filterMachine: FilterStateMachine
	private val filterData = FilterData(FilterType.IDENTITY, FilterRange.IMAGE)
	private val buildFilterStateChain = EntireImageFilterStateBuilder()
	private lateinit var histogramPresenter: HistogramPresenter
	private var imgFile: File? = null
	private val customMatrixSpinners = MutableList(0) { Spinner<Int>() }

	override fun initialize(location: URL?, resources: ResourceBundle?) {

		histogramPresenter = ChartHistogramPresenter(chartRed!!, chartGreen!!, chartBlue!!)
		imageController = ImageController(imgGroup!!, imgView!!, histogramPresenter)
		filterMachine = FilterStateMachine(NullFilteringState(imageController))
		buildFilterStateChain
				.setNext(CircleBrushFilterStateBuilder())
				.setNext(PolygonSelectionFilterStateBuilder(filterMachine))

		setupUi()
	}

	@FXML
	private fun applyFilter() {

		imageController.img?.let { filterData.img = it }

		if (filterData.filterType == FilterType.CUSTOM)
			filterMachine.applyFilter(createCustomFilter())
		else
			filterMachine.applyFilter(filterData)
	}

	@FXML
	private fun loadImage() {

		val fileChooser = FileChooser()
		fileChooser.title = "Open Resource File"
		fileChooser.extensionFilters.addAll(
				FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
				FileChooser.ExtensionFilter("PNG Files", "*.png"),
				FileChooser.ExtensionFilter("BMP Files", "*.bmp"),
		)

		imgFile = fileChooser.showOpenDialog(null)
		imgFile?.let {
			loadImgFromFile(it)
			filterPanel!!.isDisable = false
		}
	}

	@FXML
	private fun reset() = imgFile?.let { loadImgFromFile(it) }

	private fun loadImgFromFile(file: File) {

		val img = loadScaledImage(file, 800.0, 800.0)
		imageController.updateImage(img)

		filterData.img = img
		filterMachine.state = buildFilterStateChain.buildState(filterData, imageController)
	}

	private fun createCustomFilter(): ImageFilter {

		fun index(row: Int, col: Int) = row * customMatrixColumns + col

		val array = Array<Array<Int>>(customMatrixRows) { i ->
			Array(customMatrixColumns) { j ->
				customMatrixSpinners[index(i, j)].value
			}
		}

		val factor = if (autoFactorCheck!!.isSelected) null else factorSpinner!!.value

		return MatrixImageFilter(filterData.img!!, IntMatrix(array), offsetSpinner!!.value, factor)
	}

	// ---------------------------------------- UI SETUP ----------------------------------------

	private fun setupFilterTypeButton(button: RadioButton, type: FilterType) {

		button.selectedProperty().addListener { _, _, newValue ->
			run {
				if (newValue) {
					filterData.filterType = type
				}
			}
		}
	}

	private fun setupFilterRangeButton(button: RadioButton, range: FilterRange) {

		button.selectedProperty().addListener { _, _, newValue ->
			run {
				if (newValue) {
					filterData.filterRange = range
					filterMachine.state = buildFilterStateChain.buildState(filterData, imageController)
				}
			}
		}
	}

	private fun createCustomMatrixEntry() {

		customMatrixGrid?.let {

			for (i in 0 until customMatrixRows) {
				for (j in 0 until customMatrixColumns) {

					val initVal = if (i == j && i == customMatrixRows / 2) 1 else 0
					val spinner = Spinner<Int>(-100, 100, initVal)
					spinner.prefWidth = 80.0
					it.add(spinner, i, j, 1, 1)
					customMatrixSpinners.add(spinner)
				}
			}
		}
	}

	private fun setupUi() {

		brushRadiusSlider!!.onMouseReleased = EventHandler {
			filterData.img?.let { filterMachine.state = buildFilterStateChain.buildState(filterData, imageController) }
		}
		brushRadiusSlider!!.valueProperty().addListener { _, _, newValue ->
			run {
				brushRadiusSlider!!.value = round(newValue.toDouble())
			}
		}

		filterData.brushRadiusProperty.bind(brushRadiusSlider!!.valueProperty())
		brushRadiusLabel!!.textProperty().bind(brushRadiusSlider!!.valueProperty().asString())

		customFilterPane!!.disableProperty().bind(customFilterButton!!.selectedProperty().not())

		factorSpinner!!.disableProperty().bind(autoFactorCheck!!.selectedProperty())

		loadImageAction!!.accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)

		imgGroup!!.onMouseClicked = EventHandler {
			filterMachine.onMouseClicked(it)
		}
		imgGroup!!.onMouseMoved = EventHandler {
			filterMachine.onMouseMoved(it)
		}
		imgGroup!!.onMouseDragged = EventHandler {
			filterMachine.onMouseDragged(it)
		}

		setupFilterRangeButton(entireImageButton!!, FilterRange.IMAGE)
		setupFilterRangeButton(circleBrushButton!!, FilterRange.CIRCLE)
		setupFilterRangeButton(polygonButton!!, FilterRange.POLYGON)

		setupFilterTypeButton(identityButton!!, FilterType.IDENTITY)
		setupFilterTypeButton(blurButton!!, FilterType.BLUR)
		setupFilterTypeButton(sharpenButton!!, FilterType.SHARPEN)
		setupFilterTypeButton(reliefButton!!, FilterType.RELIEF)
		setupFilterTypeButton(edgeDetectButton!!, FilterType.EDGE_DETECT)
		setupFilterTypeButton(customFilterButton!!, FilterType.CUSTOM)

		createCustomMatrixEntry()
	}

	@FXML private var chartRed: AreaChart<Int, Int>? = null
	@FXML private var chartGreen: AreaChart<Int, Int>? = null
	@FXML private var chartBlue: AreaChart<Int, Int>? = null
	@FXML private var imgView: ImageView? = null
	@FXML private var imgGroup: Group? = null
	@FXML private var brushRadiusSlider: Slider? = null
	@FXML private var brushRadiusLabel: Label? = null
	@FXML private var loadImageAction: MenuItem? = null
	@FXML private var customMatrixGrid: GridPane? = null
	@FXML private var autoFactorCheck: CheckBox? = null
	@FXML private var factorSpinner: Spinner<Int>? = null
	@FXML private var offsetSpinner: Spinner<Int>? = null
	@FXML private var customFilterPane: VBox? = null
	@FXML private var filterPanel: VBox? = null

	@FXML private var entireImageButton: RadioButton? = null
	@FXML private var circleBrushButton: RadioButton? = null
	@FXML private var polygonButton: RadioButton? = null

	@FXML private var identityButton: RadioButton? = null
	@FXML private var blurButton: RadioButton? = null
	@FXML private var sharpenButton: RadioButton? = null
	@FXML private var reliefButton: RadioButton? = null
	@FXML private var edgeDetectButton: RadioButton? = null
	@FXML private var customFilterButton: RadioButton? = null
}