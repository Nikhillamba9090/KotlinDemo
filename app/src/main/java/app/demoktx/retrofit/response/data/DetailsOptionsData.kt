package app.demoktx.retrofit.response.data

class DetailsOptionsData(var id: String?, var name: String?, selected: Boolean) {
    var isSelected = false

    init {
        this.isSelected = selected
    }
}
