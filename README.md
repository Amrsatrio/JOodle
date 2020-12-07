# JOodle

#### Oodle DLL bindings for the JVM
#### Usage
	Oodle.decompress(compressedData: ByteArray, uncompressedLength: Int) returns ByteArray
	Oodle.decompress(compressedData: ByteArray, uncompressedData: ByteArray)
	Oodle.decompress(compressedData: ByteArray, compressedDataOffset: Int, compressedLength: Int, uncompressedData: ByteArray, uncompressedDataOffset: Int, uncompressedLength: Int)
	Oodle.compress(uncompressedData: ByteArray, compressor: Int, compressionLevel: Int) returns ByteArray
#### Dependency
##### Maven
- Add the repository
```xml
<repositories>
	<repository>
		<id>bintray-fungamesleaks-mavenRepo</id>
		<name>bintray</name>
		<url>https://dl.bintray.com/fungamesleaks/mavenRepo</url>
	</repository>
</repositories>
```
- Add the dependency
```xml
<dependency>
	<groupId>me.fungames</groupId>
	<artifactId>JOodle</artifactId>
	<version>1.2</version>
</dependency>
```
##### Gradle
- Add the repository
```groovy
repositories {
	maven {
		url "https://dl.bintray.com/fungamesleaks/mavenRepo"
	}
}
```
- Add the dependency
```groovy
implementation 'me.fungames:JOodle:1.2'
```

