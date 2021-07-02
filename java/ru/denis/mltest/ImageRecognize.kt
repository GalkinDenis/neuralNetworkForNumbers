package ru.denis.mltest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.math.RoundingMode
import kotlin.math.exp
import kotlin.math.pow
import kotlin.random.Random.Default.nextDouble

class ImageRecognize: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_maiz)

        //Predicting
        println("\nPredicting before training..." + "\n")
        for(b in 0..9) {
            //битовая карта изображения
            bitMap = BitmapFactory.decodeResource(resources, trainingData1[b])
            //уменьшение размера битовой карты
            bitMapCut = Bitmap.createScaledBitmap(bitMap, 10, 10, false)
            //инициализация вектора пикселей
            bitMapCut.getPixels(vectorOfPixels, 0, 10, 0, 0, 10, 10)
            for(i in vectorOfPixels.indices) {
                when (Color.red(vectorOfPixels[i])) {
                    255 -> vectorOfPixels[i] = 1
                    0 -> vectorOfPixels[i] = 0
                    else -> vectorOfPixels[i] = 1
                }
            }
            println("\n*******************************************************")
            predict(vectorOfPixels, expectedValue[b], b)
            println("*******************************************************\n")
        }

        println("////////////////////////////////////////////////////\n" +
                "////////////////////////////////////////////////////\n")

        //Training
        println("\nTraining..." + "\n")
        for(c in 0..epoch) {
            for(f in primaryTrainingData.indices) {
                for (b in trainingData1.indices) {
                    //битовая карта изображения
                    bitMap = BitmapFactory.decodeResource(resources, primaryTrainingData[f][b])
                    //уменьшение размера битовой карты
                    bitMapCut = Bitmap.createScaledBitmap(bitMap, 10, 10, false)
                    //инициализация вектора пикселей
                    bitMapCut.getPixels(vectorOfPixels, 0, 10, 0, 0, 10, 10)
                    for (i in vectorOfPixels.indices) {
                        when (Color.red(vectorOfPixels[i])) {
                            255 -> vectorOfPixels[i] = 1
                            0 -> vectorOfPixels[i] = 0
                            else -> vectorOfPixels[i] = 1
                        }
                    }
                    training(vectorOfPixels, expectedValue[b], c, b)
                }
                println("Mean square error of primaryTrainingData #$c: ${meanSquareError(lossSqrt).toBigDecimal().setScale(15, RoundingMode.UP).toDouble()}\n")
                lossSqrt.clear()
                sumOfLoss = 0.0
            }
        }

        println("////////////////////////////////////////////////////\n" +
                "////////////////////////////////////////////////////\n")

        //Predicting
        println("\nPredicting after training..." + "\n")
        for(b in 0..9) {
            //битовая карта изображения
            bitMap = BitmapFactory.decodeResource(resources, testData[b])
            //уменьшение размера битовой карты
            bitMapCut = Bitmap.createScaledBitmap(bitMap, 10, 10, false)
            //инициализация вектора пикселей
        bitMapCut.getPixels(vectorOfPixels, 0, 10, 0, 0, 10, 10)
            for (i in vectorOfPixels.indices) {
                when (Color.red(vectorOfPixels[i])) {
                    255 -> vectorOfPixels[i] = 1
                    0 -> vectorOfPixels[i] = 0
                    else -> vectorOfPixels[i] = 1
                }
            }
            println("\n*******************************************************")
            predict(vectorOfPixels, expectedValue[b], b)
            println("*******************************************************\n")
        }
    }
}

const val epoch = 5000
const val learningRate = 0.2

private lateinit var bitMap: Bitmap
private lateinit var bitMapCut: Bitmap
private var vectorOfPixels = IntArray(100)

//Sets of training
private val trainingData1 = arrayOf(
    R.drawable.zero,
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six,
    R.drawable.seven,
    R.drawable.eith,
    R.drawable.nine,
)

private val trainingData2 = arrayOf(
    R.drawable.zero2,
    R.drawable.one2,
    R.drawable.two2,
    R.drawable.three2,
    R.drawable.four2,
    R.drawable.five2,
    R.drawable.six2,
    R.drawable.seven2,
    R.drawable.eight2,
    R.drawable.nine2,
)

private val trainingData3 = arrayOf(
    R.drawable.zeroq,
    R.drawable.oneq,
    R.drawable.twoq,
    R.drawable.threeq,
    R.drawable.fourq,
    R.drawable.fiveq,
    R.drawable.sixq,
    R.drawable.sevenq,
    R.drawable.eightq,
    R.drawable.nineq
)

private val trainingData4 = arrayOf(
    R.drawable.testzero,
    R.drawable.testone,
    R.drawable.testtwo,
    R.drawable.testthree,
    R.drawable.testfour,
    R.drawable.testfive,
    R.drawable.testsex,
    R.drawable.testseven,
    R.drawable.testeight,
    R.drawable.testnine
)

private val primaryTrainingData = arrayOf(
    trainingData1,
    trainingData2,
    trainingData3,
    trainingData4
)

private val testData = arrayOf(
    R.drawable.zero3,
    R.drawable.one3,
    R.drawable.two3,
    R.drawable.three3,
    R.drawable.four3,
    R.drawable.five3,
    R.drawable.six3,
    R.drawable.seven3,
    R.drawable.eight3,
    R.drawable.nine3,
)

//Expected values for each set training
private val expectedValue = arrayOf(
    arrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0),
    arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0),
)

var lossSqrt = arrayListOf<Double>()

var sumError = 0.0
var sumOfLoss = 0.0

var sumOfHiddenNeuronAll = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)
var sumsOfOutputNeuronAll = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

var activationValuesOfAllHiddenNeurons = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)
var activationValueOfOutputNeuronsAll = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

var hiddenNeuronsWeightsAll = arrayOf(
    Array(100) {nextDouble()},
    Array(100) {nextDouble()},
    Array(100) {nextDouble()},
    Array(100) {nextDouble()},
    Array(100) {nextDouble()}
)

var outputNeuronsWeightsAll = arrayOf(
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()},
    Array(5) {nextDouble()}
)

var errorOfAllHiddenNeurons = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)

var weightDeltaOfAllHiddenNeuron = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)
var weightDeltaOfOutputNeuronAll = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

fun training(trainingData: IntArray, expectedValue: Array<Double>, count: Int, trainingSet: Int) {

    for(z in hiddenNeuronsWeightsAll.indices) {
        sumOfHiddenNeuronAll[z] = 0.0
        for(x in trainingData.indices) {
            sumOfHiddenNeuronAll[z] += trainingData[x] * hiddenNeuronsWeightsAll[z][x]
        }
        activationValuesOfAllHiddenNeurons[z] = activationFunction(sumOfHiddenNeuronAll[z])
    }

    for(a in outputNeuronsWeightsAll.indices) {
        sumsOfOutputNeuronAll[a] = 0.0
        for(b in activationValuesOfAllHiddenNeurons.indices) {
            sumsOfOutputNeuronAll[a] += activationValuesOfAllHiddenNeurons[b] * outputNeuronsWeightsAll[a][b]
        }
    }

    for(q in sumsOfOutputNeuronAll.indices) {
        activationValueOfOutputNeuronsAll[q] = activationFunction(sumsOfOutputNeuronAll[q])
    }

    sumError = 0.0
    for(i in activationValueOfOutputNeuronsAll.indices) {
        sumError += activationValueOfOutputNeuronsAll[i] - expectedValue[i]
    }

    lossSqrt.add(sumError)

    for(i in weightDeltaOfOutputNeuronAll.indices) {
        weightDeltaOfOutputNeuronAll[i] = weightsDelta(error(activationValueOfOutputNeuronsAll[i], expectedValue[i]), sigmoidDx(activationValueOfOutputNeuronsAll[i])
        )
    }

    for(i in activationValueOfOutputNeuronsAll.indices) {
        activationValueOfOutputNeuronsAll[i] = 0.0
    }

    for(c in outputNeuronsWeightsAll.indices) {
        for(b in hiddenNeuronsWeightsAll.indices) {
            outputNeuronsWeightsAll[c][b] = backPropagation(outputNeuronsWeightsAll[c][b], activationValuesOfAllHiddenNeurons[b], weightDeltaOfOutputNeuronAll[c], learningRate
            )
        }
    }

    for(i in errorOfAllHiddenNeurons.indices) {
        errorOfAllHiddenNeurons[i] = 0.0
    }

    for(c in errorOfAllHiddenNeurons.indices) {
        for(b in outputNeuronsWeightsAll.indices) {
            errorOfAllHiddenNeurons[c] += outputNeuronsWeightsAll[b][c] * weightDeltaOfOutputNeuronAll[b]
        }
    }

    for(c in hiddenNeuronsWeightsAll.indices) {
        weightDeltaOfAllHiddenNeuron[c] = weightsDelta(errorOfAllHiddenNeurons[c], sigmoidDx(activationValuesOfAllHiddenNeurons[c]))
    }

    for(c in hiddenNeuronsWeightsAll.indices) {
        for(b in hiddenNeuronsWeightsAll[0].indices){
            hiddenNeuronsWeightsAll[c][b] = backPropagation(hiddenNeuronsWeightsAll[c][b], trainingData[b].toDouble(), weightDeltaOfAllHiddenNeuron[c], learningRate)
        }
    }
}

fun predict(trainingData: IntArray, expectedValue: Array<Double>, trainingSet: Int) {

    for(z in hiddenNeuronsWeightsAll.indices) {
        sumOfHiddenNeuronAll[z] = 0.0
        for(x in trainingData.indices) {
            sumOfHiddenNeuronAll[z] += trainingData[x] * hiddenNeuronsWeightsAll[z][x]
        }
        activationValuesOfAllHiddenNeurons[z] = activationFunction(sumOfHiddenNeuronAll[z])
    }

    println("INPUT #$trainingSet\nHIDDEN NEURONS:")
    var neuronCount = 0
    sumOfHiddenNeuronAll.forEach {
        println(
            "\n${(neuronCount) + 1}st hidden neuron: sum of (weight * input) -> $it  |  sigmoid(output) -> " +
                    activationValuesOfAllHiddenNeurons[neuronCount] + "\n"
        )
        neuronCount++
    }

    for(a in outputNeuronsWeightsAll.indices) {
        sumsOfOutputNeuronAll[a] = 0.0
        for(b in activationValuesOfAllHiddenNeurons.indices) {
            sumsOfOutputNeuronAll[a] += activationValuesOfAllHiddenNeurons[b] * outputNeuronsWeightsAll[a][b]
        }
    }

    for(q in sumsOfOutputNeuronAll.indices) {
        activationValueOfOutputNeuronsAll[q] = activationFunction(sumsOfOutputNeuronAll[q])
    }

    neuronCount = 0
    println("\nOUTPUT NEURONS:")
    sumsOfOutputNeuronAll.forEach {
        println(
            "\nPredict for #$neuronCount number, ${(neuronCount) + 1}st output neuron: sum of (weight * input) -> $it  |  sigmoid(output) -> " +
                    activationValueOfOutputNeuronsAll[neuronCount].toBigDecimal().setScale(3, RoundingMode.UP).toDouble() +
                    " | predictable value ->  ${expectedValue[neuronCount]}\n"
        )
        neuronCount++
    }
}

fun activationFunction(x: Double): Double {
    return 1 / (1 + exp(-x))
}

fun meanSquareError(array: ArrayList<Double>): Double {
    for(c in array.indices) {
        sumOfLoss += array[c].pow(2)
    }
    sumOfLoss /= array.size
    return sumOfLoss
}

fun error(actual: Double, expected: Double): Double = actual - expected
fun sigmoidDx(actual: Double): Double = actual * (1.0 - actual)
fun weightsDelta(error: Double, sigmoidDx: Double): Double = error * sigmoidDx
fun backPropagation(weight: Double, pastOutput: Double, weightDelta: Double, learningRate: Double): Double = weight - (pastOutput * weightDelta * learningRate)