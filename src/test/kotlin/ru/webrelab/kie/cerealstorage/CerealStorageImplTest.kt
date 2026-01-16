package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(4f, 3f)
        }
    }

    @Test
    fun `should create CerealStorageImpl if storageCapacity more than containerCapacity`() {
        val storage = CerealStorageImpl(3f, 5f)
        assertEquals(3f, storage.containerCapacity)
        assertEquals(5f, storage.storageCapacity)
    }

    @Test
    fun `should create CerealStorageImpl if storageCapacity = containerCapacity`() {
        val storage = CerealStorageImpl(3f, 3f)
        assertEquals(3f, storage.containerCapacity)
        assertEquals(3f, storage.storageCapacity)
    }

    @Test
    fun `addCereal should throw IllegalArgumentException for negative amount`() {
        val storage = CerealStorageImpl(3f, 3f)
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, -3f)
        }
    }

    @Test
    fun `should throw IllegalStateException when adding new container exceeds storage capacity`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        storage.addCereal(Cereal.MILLET, 10f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.PEAS, 10f)
        }
    }

    @Test
    fun `addCereal should return overflow amount when storage is full`() {
        val result = storage.addCereal(Cereal.BUCKWHEAT, 12f)
        assertEquals(2f, result)
    }

    @Test
    fun `addCereal should return overflow amount when storage is full with fractional numbers`() {
        val result = storage.addCereal(Cereal.BUCKWHEAT, 12.01f)
        assertEquals(2.01f, result, 0.01f)
    }

    @Test
    fun `addCereal should return overflow amount when storage is full across multiple calls`() {
        storage.addCereal(Cereal.BUCKWHEAT, 8f)
        val result = storage.addCereal(Cereal.BUCKWHEAT, 8f)
        assertEquals(6f, result)
    }

    @Test
    fun `addCereal should return 0 when storage is not full`() {
        val result = storage.addCereal(Cereal.BUCKWHEAT, 2f)
        assertEquals(0f, result)
    }

    @Test
    fun `addCereal should return 0 when storage is not full across multiple calls`() {
        storage.addCereal(Cereal.BUCKWHEAT, 2f)
        val result = storage.addCereal(Cereal.BUCKWHEAT, 2f)
        assertEquals(0f, result)
    }

    @Test
    fun `getCereal should throw IllegalArgumentException if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.PEAS, -5f)
        }
    }

    @Test
    fun `getCereal should return 0 if container does not exist`() {
        val result = storage.getCereal(Cereal.PEAS, 6f)
        assertEquals(0f, result)
    }

    @Test
    fun `getCereal should return rest of the cereal if there was less cereal in the container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        val result = storage.getCereal(Cereal.BUCKWHEAT, 6f)
        assertEquals(5f, result)
    }

    @Test
    fun `getCereal should return requested amount when container has sufficient cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        val result = storage.getCereal(Cereal.BUCKWHEAT, 6f)
        assertEquals(6f, result)
    }

    @Test
    fun `getCereal should return requested amount when container has sufficient cereal with fractional numbers`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        val result = storage.getCereal(Cereal.BUCKWHEAT, 6.02f)
        assertEquals(6.02f, result, 0.01f)
    }

    @Test
    fun `removeContainer should return true if container is empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        val result = storage.removeContainer(Cereal.BUCKWHEAT)
        assertEquals(true, result)
    }

    @Test
    fun `removeContainer should return false if container is not empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 1f)
        val result = storage.removeContainer(Cereal.BUCKWHEAT)
        assertEquals(false, result)
    }

    @Test
    fun `removeContainer should return false if container is not exist`() {
        val result = storage.removeContainer(Cereal.BUCKWHEAT)
        assertEquals(false, result)
    }

    @Test
    fun `getAmount should return 0 if container is not exist`() {
        val result = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(0f, result)
    }

    @Test
    fun `getAmount should return 0 if container is empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        val result = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(0f, result)
    }

    @Test
    fun `getAmount should amount cereal in container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        val result = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(10f, result)
    }

    @Test
    fun `getAmount should amount cereal in container with fractional numbers`() {
        storage.addCereal(Cereal.BUCKWHEAT, 7.771f)
        val result = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(7.771f, result, 0.01f)
    }

    @Test
    fun `getSpace throw IllegalStateException if container is not exist`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.PEAS)
        }
    }

    @Test
    fun `getSpace should return if container is full`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        val result = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(0f, result)
    }

    @Test
    fun `getSpace should return remaining space considering current fill`() {
        storage.addCereal(Cereal.BUCKWHEAT, 8f)
        val result = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(2f, result)
    }

    @Test
    fun `getSpace should return remaining space considering current fill with fractional numbers`() {
        storage.addCereal(Cereal.BUCKWHEAT, 8.22f)
        val result = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(1.78f, result, 0.01f)
    }

    @Test
    fun `toString should return error message if storage is empty`() {
        val result = storage.toString()
        assertEquals("Storage is empty", result)
    }

    @Test
    fun `toString should return information about storage`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        storage.addCereal(Cereal.MILLET, 10f)
        val result = storage.toString()
        assertEquals(
            "storageCapacity: ${storage.storageCapacity}, containerCapacity: ${storage.containerCapacity}\nCereal: BUCKWHEAT, amount: 10.0\nCereal: MILLET, amount: 10.0",
            result
        )
    }
}