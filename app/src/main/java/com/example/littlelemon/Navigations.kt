package com.example.littlelemon

interface Destination{
    var route:String
}
object SignInPage:Destination{
    override var route="SignUpPage"
}
object LogInPage:Destination{
    override var route="LogInPage"
}
object HomePage: Destination{
    override var route="HomePage"
}

object ProfilePage: Destination{
    override var route="Profile"
}