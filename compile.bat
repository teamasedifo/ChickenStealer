@echo off
echo Running Java class compiler...

if defined JAVA_HOME (
if exist "%JAVA_HOME%\bin\javac.***" goto okJavac
)
set JAVA_ROOT=%ProgramFiles%\Java\
if not exist "%JAVA_ROOT%\" set JAVA_ROOT=%ProgramFiles(x86)%\Java\
if not exist "%JAVA_ROOT%\" set JAVA_ROOT=%SystemDrive%\Java\
if not exist "%JAVA_ROOT%\" set JAVA_ROOT=%HOMEDRIVE%\Java\
if not exist "%JAVA_ROOT%\" set JAVA_ROOT=%SystemDrive%\
echo Search Java JDK...
for /F "usebackq delims==" %%f in (`dir "%JAVA_ROOT%jdk*" /B /O:-N`) do if exist "%JAVA_ROOT%%%f\bin\javac.***" (
set JAVA_HOME="%JAVA_ROOT%%%f"
goto foundJava
)
echo Search Java JRE...
for /F "usebackq delims==" %%f in (`dir "%JAVA_ROOT%jre*" /B /O:-N`) do if exist "%JAVA_ROOT%%%f\bin\javac.***" (
set JAVA_HOME="%JAVA_ROOT%%%f"
goto foundJava
)
:foundJava
for /F "useback tokens=*" %%s in ('%JAVA_HOME%') do set JAVA_HOME=%%~s
if exist "%JAVA_HOME%\bin\javac.***" goto okJavac
echo Cannot find Java compiler. Please install Java JDK.
goto end
:okJavac
echo Java path "%JAVA_HOME%"
"%JAVA_HOME%\bin\javac.exe" -version

subst m: %0\..
pushd m:
cd \
echo Search java sources in .\src\...
dir src\*.java /B/S > javasrc.tmp~
if ERRORLEVEL 1 (
echo Cannot find Java source files in .\src\
goto abort
)
echo Search jar libraries in .\lib\...
if exist lib for /F "usebackq delims==" %%l in (`dir lib\*.jar /B/S`) do echo -classpath %%l >> javasrc.tmp~
echo Compile in .\bin\...
if exist bin rmdir /S /Q bin
mkdir bin
echo on
@"%JAVA_HOME%\bin\javac.exe" -d bin @javasrc.tmp~
@echo off
echo Done.
:abort
del javasrc.tmp~
popd
subst m: /d

:end
set JAVA_ROOT=
pause
echo on