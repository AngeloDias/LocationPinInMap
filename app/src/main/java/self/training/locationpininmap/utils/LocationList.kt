package self.training.locationpininmap.utils

class LocationList {
    var locationsPins = ArrayList<Location>()

    constructor() : super()

    constructor(Locations : ArrayList<Location>) : super() {
        this.locationsPins = Locations
    }
}