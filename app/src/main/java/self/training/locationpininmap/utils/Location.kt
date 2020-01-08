package self.training.locationpininmap.utils

class Location {
    var nome : String? = null
    var categoria : Int? = null
    var latitude : Double? = null
    var longitude : Double? = null

    constructor() : super()

    constructor(Nome : String, Categoria : Int, Latitude : Double, Longitude : Double) : super() {
        this.nome = Nome
        this.categoria = Categoria
        this.latitude = Latitude
        this.longitude = Longitude
    }

}