package bg.dabulgaria.tibroish.domain.image

interface IGalleryImagesRepository {

    fun getImages():List<PickedImage>
}