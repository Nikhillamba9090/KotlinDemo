package app.demoktx.retrofit

/**
 * Created by appsinvo on 13/11/18.
 */

interface IApiCallback {

    /**
     * Method for getting the type and data.
     *
     * @param data Actual data
     */
    fun onSuccess(type: Any, data: Any, extraData: Any)

    /**
     * Failure Reason
     * @param data
     */
    fun onFailure(data: Any)
}
