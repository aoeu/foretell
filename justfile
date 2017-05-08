device = $(shell adb devices | grep '[0-9]' | head -1 | cut -d'	' -f1)
packageName = $(xmllint -xpath 'string(//manifest/@package)' AndroidManifest.xml)
mainActivityName = $(sed -e 's/android://g' AndroidManfiest.xml | xmllint -xpath 'string(//activity[descendant::action[@name="android.intent.action.MAIN"]]/@name)' - )

appName = app
apkPath = $(appName).apk
adb = adb -s $(device)

default: bind assemble install start

install-gomobile:
	$ANDROID_HOME/tools/bin/sdkmanager ndk-bundle && \
	go get golang.org/x/mobile/cmd/gomobile && \
	gomobile init -ndk $ANDROID_HOME/ndk-bundle

bind:
	gomobile bind -target=android github.com/aoeu/foretell/noaa && mv noaa.aar lib/
	
assemble:
	(./gradlew assembleDebug 2>&1 | grep -v '^:.*:.*' | grep -v 'incubating') 1>&2

compile:
	(./gradlew compileDebugJavaWithJavac 2>&1 | grep -v '^:.*:.*' | grep -v 'incubating') 1>&2

install:
	$(adb) install -r $(apkPath)

start:
	$(adb) shell am start -n $(packageName)/$(packageName)$(mainActivityName)
