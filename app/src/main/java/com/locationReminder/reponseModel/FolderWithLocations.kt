package com.locationReminder.reponseModel

data class FolderWithLocations(
    val folder: CategoryFolderResponseModel,
    val locations: List<LocationDetail>
)
