package alves.ariel.pomodoroapp.presentation.home

import alves.ariel.pomodoroapp.presentation.home.EstadoTimer.PAUSA_CURTA
import alves.ariel.pomodoroapp.presentation.home.EstadoTimer.PAUSA_LONGA
import alves.ariel.pomodoroapp.presentation.home.EstadoTimer.POMODORO
import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Timer(
    var _pomodoro: Int = 30,
    var _pausaCurta: Int = 5,
    var _pausaLonga: Int = 15
)

enum class EstadoTimer {
    POMODORO,
    PAUSA_CURTA,
    PAUSA_LONGA
}

class HomeViewModel : ViewModel() {

    private val timer = Timer()
    private val _estadoPomodoro = MutableLiveData(0)
    private val _tempoRestante = MutableLiveData<String>()
    val tempoRestante: LiveData<String> = _tempoRestante
    private val _progresso = MutableLiveData<Float>(0f)
    val progresso: LiveData<Float> = _progresso
    val estadoPomodoro: LiveData<Int> = _estadoPomodoro
    private var estadoAtual = POMODORO
    var pomodorosConcluidos = 0
    private val pomodoro = timer._pomodoro
    private val pausaCurta = timer._pausaCurta
    private val pausaLonga = timer._pausaLonga
    var _contador: CountDownTimer? = null
    var tempoInicial = pomodoro



    // Função para criar um novo contador
    private fun criarContador(tempoEmMilissegundos: Long): CountDownTimer {
        return object : CountDownTimer(tempoEmMilissegundos, 1000) {
            // Função chamada a cada intervalo de tempo
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                _tempoRestante.value = String.format("%02d:%02d", minutos, segundos)

                _progresso.value =
                    ((tempoEmMilissegundos - millisUntilFinished).toFloat() / tempoEmMilissegundos.toFloat()) * 100

                println("Tempo restante: $minutos minutos e $segundos segundos")


            }

            override fun onFinish() {

                when (estadoAtual) {
                    POMODORO -> {
                        pomodorosConcluidos++
                        if (pomodorosConcluidos % 4 == 0) {
                            estadoAtual = PAUSA_LONGA
                            pausaLonga()

                        } else {
                            estadoAtual = PAUSA_CURTA
                            pausaCurta()
                        }
                    }

                    PAUSA_CURTA -> {
                        estadoAtual = POMODORO
                        pomodoro()

                    }
                    PAUSA_LONGA -> {
                        estadoAtual = POMODORO
                        pomodoro()
                        pomodorosConcluidos = 0 //Reinicia o contador após uma pausa longa
                    }


                }


            }


        }
    }

    internal fun defineTimer(tempo: Int) {

        // Cancela o contador atual, se houver
        _contador?.cancel()
        val tempoEmMilissegundos = tempo * 60 * 1000
        _contador = criarContador(tempoEmMilissegundos.toLong())


    }

    fun pomodoro() {
        defineTimer(pomodoro)
        tempoInicial = pomodoro
        _estadoPomodoro.value = 0
    }

    fun pausaCurta() {
        defineTimer(pausaCurta)
        tempoInicial = pausaCurta
        _estadoPomodoro.value = 1
    }

    fun pausaLonga() {
        defineTimer(pausaLonga)
        tempoInicial = pausaLonga
        _estadoPomodoro.value = 2
    }


}