package bg.dabulgaria.tibroish.presentation.providers

import javax.inject.Inject

interface ICameraTakenImageProvider{

    var cameraImagePath:String
}

class CameraTakenImageProvider @Inject constructor() : ICameraTakenImageProvider {

    override var cameraImagePath:String =""
}