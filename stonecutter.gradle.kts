stonecutter active "26.3"

stonecutter parameters {
    swaps["mod_version"] = "\"${project.version}\";"
    swaps["minecraft"] = "\"${current.version}\";"

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }

        string(current.parsed >= "26.1") {
            replace("classTweaker v2 named", "classTweaker v2 official")
        }
    }
}
