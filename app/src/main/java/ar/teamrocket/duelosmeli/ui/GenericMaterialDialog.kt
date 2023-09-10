package ar.teamrocket.duelosmeli.ui

import android.content.Context
import ar.teamrocket.duelosmeli.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Clase para construir dialogs de Material Design de acuerdo a como querramos configurarlos
 * De no encontrar un constructor adecuado para algún requerimiento, agregarlo
 */
class GenericMaterialDialog {
    val context: Context
    val title: Int
    val message: Int?
    val negativeButton: Int?
    val positiveButton: Int?
    val negativeFun: (() -> Unit)?
    val positiveFun: (() -> Unit)?

    /**
     * Constructor completo con todos los parametros
     * @param [context] contexto de la activity
     * @param [title] titulo
     * @param [message] descripción
     *
     * Los botones no necesariamente deben estar relacionado a una acción "negativa" o "positiva"
     * @param [negativeButton] texto del botón
     * @param [positiveButton] texto del botón
     * @param [negativeFun] se recibe una función para ejecutar con el botón
     * @param [positiveFun] se recibe una función para ejecutar con el botón
     */
    constructor(context: Context, title: Int, message: Int, negativeButton: Int, positiveButton: Int, negativeFun: (() -> Unit), positiveFun: (() -> Unit)) {
        this.context = context
        this.title = title
        this.message = message
        this.negativeButton = negativeButton
        this.positiveButton = positiveButton
        this.negativeFun = negativeFun
        this.positiveFun = positiveFun
    }

    constructor(context: Context, title: Int) {
        this.context = context
        this.title = title
        this.message = null
        this.negativeButton = null
        this.positiveButton = null
        this.negativeFun = null
        this.positiveFun = null
    }

    /**
     * Construye el dialog de acuerdo al constructor que se utilice
     */
    fun buildDialog() {
        val builder = MaterialAlertDialogBuilder(context, R.style.Dialog)
            .setTitle(title)

        message?.let { builder.setMessage(it) }

        negativeButton?.let {
            builder.setNegativeButton(it) { _, _ ->
                negativeFun?.invoke()
            }
        }

        positiveButton?.let {
            builder.setPositiveButton(it) { _, _ ->
                positiveFun?.invoke()
            }
        }

        builder.show()
    }
}
