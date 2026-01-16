package ru.webrelab.kie.cerealstorage

import kotlin.math.min

class CerealStorageImpl(
    override val containerCapacity: Float, override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(
        cereal: Cereal, amount: Float
    ): Float {
        if (amount < 0f) throw IllegalArgumentException("Amount is negative")

        if (storage.containsKey(cereal)) {
            val currentAmount = storage[cereal] ?: 0f
            val freeSpace = containerCapacity - currentAmount

            if (freeSpace == 0f) return amount
            else if (amount <= freeSpace) {
                storage[cereal] = storage.getValue(cereal).plus(amount)
                return 0f
            } else {
                storage[cereal] = storage.getValue(cereal).plus(freeSpace)
                return amount - freeSpace
            }
        } else {
            val numberContainers = storage.size
            val storageSpaceOccupied = numberContainers * containerCapacity

            if ((storageSpaceOccupied + containerCapacity) > storageCapacity) throw IllegalStateException(
                "Cannot add new storage container"
            ) else {
                storage[cereal] = min(amount, containerCapacity)
                return if (amount <= containerCapacity) {
                    0f
                } else {
                    amount - containerCapacity
                }
            }
        }
    }

    override fun getCereal(
        cereal: Cereal, amount: Float
    ): Float {
        if (amount < 0f) throw IllegalArgumentException("Amount is negative")

        if (storage.containsKey(cereal)) {
            val currentAmount = storage[cereal] ?: 0f

            if (currentAmount == 0f) return 0f
            else if (currentAmount > amount) {
                storage[cereal] = currentAmount - amount
                return amount
            } else {
                storage[cereal] = 0f
                return currentAmount
            }
        } else return 0f
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val amount = storage[cereal] ?: return false

        if (amount == 0f) {
            storage.remove(cereal)
            return true
        } else return false
    }

    override fun getAmount(cereal: Cereal): Float = storage[cereal] ?: 0f

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage[cereal] ?: throw IllegalStateException("Container is not exist")
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        if (storage.isEmpty()) return "Storage is empty"

        val result =
            storage.map { (key, value) -> "Cereal: $key, amount: $value" }.joinToString("\n")
        return "storageCapacity: $storageCapacity, containerCapacity: $containerCapacity\n$result"
    }
}