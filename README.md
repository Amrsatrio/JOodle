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
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
- Add the dependency
```xml
<dependency>
	<groupId>com.github.FabianFG</groupId>
	<artifactId>JOodle</artifactId>
	<version>1.3</version>
</dependency>
```
##### Gradle
- Add the repository
```groovy
repositories {
	maven { url 'https://jitpack.io' }
}
```
- Add the dependency
```groovy
implementation 'com.github.FabianFG:JOodle:1.3'
```
