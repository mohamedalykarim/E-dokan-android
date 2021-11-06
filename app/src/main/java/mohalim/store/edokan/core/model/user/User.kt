package mohalim.store.edokan.core.model.user

data class User(
        var userId : String,
        var phoneNumber : String,
        var userName : String,
        var email : String,
        var defaultAddress: String,
        var defaultAddressId: Int,
        var profileImage : String,
        var isSeller : Boolean,
        var isDelivery : Int,
        var isDeliverySupervisor : Int,
        var wtoken : String
    )
