arrayOf("gradle.properties", "gradle").forEach(::copyToBuildSrc)

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WeatherApp"
include(":androidApp")
include(":shared")
include(":flowredux")

fun copyToBuildSrc(sourcePath: String) {
    rootDir.resolve(sourcePath).copyRecursively(
        target = rootDir.resolve("buildSrc").resolve(sourcePath),
        overwrite = true
    )
    rootDir.resolve(sourcePath).copyRecursively(
        target = rootDir.resolve("buildSrc")
            .resolve("buildSrc")
            .resolve(sourcePath),
        overwrite = true
    )
}
