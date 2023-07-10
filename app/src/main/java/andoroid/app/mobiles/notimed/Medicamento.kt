package andoroid.app.mobiles.notimed

data class Medicamento(
    //todos los atributos que tenga medicamento
    val id: String,
    val name: String,
    val dosis: String,
    val stock: String,
    val horarios: List<String> = emptyList()
        )