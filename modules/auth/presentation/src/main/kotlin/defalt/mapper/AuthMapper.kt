package defalt.mapper

import defalt.models.LoginResponse
import defalt.valueobject.AccessToken

fun AccessToken.toLoginResponse(): LoginResponse =
    LoginResponse(
        token = this.value,
    )
