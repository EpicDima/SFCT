package com.epicdima.sfct.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.databinding.ItemCheckedBinding

/**
 * @author EpicDima
 */
abstract class EnumCheckedAdapter<E : Enum<E>, VH : EnumCheckedAdapter.EnumViewHolder<E>>(
    context: Context,
    private val changeListener: () -> Unit,
    private val enumClass: Class<E>
) : RecyclerView.Adapter<VH>() {

    private val onPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            changeListener()
        }
    }

    private val values: Array<E> = enumClass.enumConstants!!
    val map: Map<E, EnumDataHolder<E>> = createMap(context)

    private fun createMap(context: Context): Map<E, EnumDataHolder<E>> {
        val enumMap = context.getEnumStrings(enumClass)
        return buildMap {
            this@EnumCheckedAdapter.values.forEach {
                val dataHolder = EnumDataHolder(it, enumMap[it]!!)
                dataHolder.selected.addOnPropertyChangedCallback(onPropertyChangedCallback)
                this[it] = dataHolder
            }
        }
    }

    fun clear() {
        map.values.forEach { it.selected.set(false) }
    }

    fun select(list: List<E>) {
        list.forEach { map[it]?.selected?.set(true) }
    }

    fun getSelected(): List<E> {
        return map.values.filter { it.selected.get() }.map { it.value }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(map.getValue(values[position]))
    }

    protected fun createBinding(parent: ViewGroup): ItemCheckedBinding {
        return ItemCheckedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun getItemCount(): Int = values.size


    class EnumDataHolder<E>(
        val value: E,
        val text: String,
        var selected: ObservableBoolean = ObservableBoolean(false)
    )


    class EnumViewHolder<E>(
        private val binding: ItemCheckedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(value: EnumDataHolder<E>) {
            binding.value = value
            binding.layout.setOnClickListener { value.selected.set(!value.selected.get()) }
            binding.executePendingBindings()
        }
    }
}