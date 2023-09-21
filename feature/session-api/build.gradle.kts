plugins {
    id("droidknights.android.library-no-hilt")
    id("droidknights.android.compose")
}

android {
    namespace = "com.droidknights.app2023.feature.session.api"
}

dependencies {
    implementation(projects.feature.mainNavGraph)
    implementation(projects.core.repo.session.api)

    implementation(libs.androidx.compose.navigation)
}
