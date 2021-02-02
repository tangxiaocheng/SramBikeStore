package app.sram.bikestore.data

/*
* Using sealed class to wrap up the result model
*
* */
sealed class ResultModel<T>(
    open val data: T?,
    open val errorModel: ErrorModel?
)

data class Success<T>(override val data: T) : ResultModel<T>(data, null)
data class Failure<T>(override val errorModel: ErrorModel) : ResultModel<T>(null, errorModel)
