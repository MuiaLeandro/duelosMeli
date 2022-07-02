package ar.teamrocket.duelosmeli.ui.duelActivities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.teamrocket.duelosmeli.data.model.ItemDuel

class DuelGameViewModel:ViewModel() {
    lateinit var itemsDuel: List<ItemDuel>
    val itemDuel = MutableLiveData<ItemDuel>()
    val countItem = MutableLiveData<Int>()
    val score = MutableLiveData<Int>()

    fun initViewModel(items:List<ItemDuel>){
        itemsDuel = items
        countItem.value = 0
        itemDuel.value = itemsDuel[countItem.value ?: 0]
    }
    fun nextItem(){
        countItem.value = countItem.value?.plus(1) ?: 0
        itemDuel.value = itemsDuel[countItem.value ?: 0]
    }
}