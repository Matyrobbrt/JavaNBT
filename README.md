# JavaNBT
A library for using .dat files (the minecraft nbt format) in java

## Add the library as a gradle dependency
This library requires Google GSON and slf4j in order to work!
Before installing, please decide on what version you want, by seeing the available versions [here](https://cloudsmith.io/~matyrobbrt/repos/javanbt/packages/). <br>
We will start add the repository for the library. In the repositories block add:
```groovy
maven {
  url "https://dl.cloudsmith.io/public/matyrobbrt/javanbt/maven/"
}
```
From here, things are pretty straightforward. We can define the dependency, in the dependencies block using:
```groovy
implementation group: 'io.github.matyrobbrt', name: 'javanbt', version: "${javanbt_version}" // Make sure to define the javanbt_version
```
## Acknowledgements
[![Hosted By: Cloudsmith](https://img.shields.io/badge/OSS%20hosting%20by-cloudsmith-blue?logo=cloudsmith&style=for-the-badge)](https://cloudsmith.com)

Package repository hosting is graciously provided by  [Cloudsmith](https://cloudsmith.com).
Cloudsmith is the only fully hosted, cloud-native, universal package management solution, that
enables your organization to create, store and share packages in any format, to any place, with total
confidence.
