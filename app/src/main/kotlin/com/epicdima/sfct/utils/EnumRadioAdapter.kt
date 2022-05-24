package com.epicdima.sfct.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.databinding.ItemRadioBinding
import java.util.*

/**
 * @author EpicDima
 */
abstract class EnumRadioAdapter<E : Enum<E>, VH : EnumRadioAdapter.EnumViewHolder<E>>(
    context: Context,
    private val selectListener: (E) -> Unit,
    private val enumClass: Class<E>
) : RecyclerView.Adapter<VH>() {

    private val values: Array<E> = enumClass.enumConstants!!
    private val map: Map<E, EnumDataHolder<E>> = createMap(context)

    private fun createMap(context: Context): Map<E, EnumDataHolder<E>> {
        val enumMap: EnumMap<E, String> = context.getEnumStrings(enumClass)
        return buildMap {
            this@EnumRadioAdapter.values.forEach {
                this[it] = EnumDataHolder(it, enumMap[it]!!)
            }
        }
    }

    fun select(selected: E) {
        map[selected]?.selected?.set(true)
    }

    protected fun createBinding(parent: ViewGroup): ItemRadioBinding {
        return ItemRadioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(map.getValue(values[position])) { selectListener(it) }
    }

    override fun getItemCount(): Int = values.size


    class EnumDataHolder<E>(
        val value: E,
        val text: String,
        var selected: ObservableBoolean = ObservableBoolean(false)
    )

    class EnumViewHolder<E>(
        private val binding: ItemRadioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            value: EnumDataHolder<E>,
            selectListener: (E) -> Unit
        ) {
            binding.value = value
            binding.layout.setOnClickListener { selectListener(value.value) }
            binding.radiobutton.setOnClickListener { selectListener(value.value) }
            binding.executePendingBindings()
        }
    }
}