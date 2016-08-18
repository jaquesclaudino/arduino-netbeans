
ARDUINO_LIB_DIR = ${ARDUINO_BASE_DIR}/libraries
LIB_CORE_DIR = lib/core
LIB_LIBS_DIR = lib/libs
ARDUINO_LIB_CORE = ${LIB_CORE_DIR}/arduinocore.a
ARDUINO_LIB_LIBS = ${LIB_LIBS_DIR}/arduinolibs.a
AVR_DUDE = /usr/bin/avrdude -C /etc/avrdude/avrdude.conf
	
INCLUDE = -c -g \
-I${ARDUINO_CORE_DIR} \
-I${ARDUINO_PINS_DIR} \
$(patsubst %,-I${ARDUINO_LIB_DIR}/%,$(subst ;, ,$(INCLUDE_LIBS)))

FLAGS_GCC = -c -g -Os -Wall -ffunction-sections -fdata-sections -mmcu=${ARDUINO_MODEL} -DF_CPU=16000000L -MMD -DUSB_VID=null -DUSB_PID=null -DARDUINO=${ARDUINO_VERSION}
FLAGS_GPP = ${FLAGS_GCC} -fno-exceptions
FLAGS_LINKER = ${ARDUINO_LIB_CORE} ${ARDUINO_LIB_LIBS} -Os -Wl,--gc-sections,--relax -mmcu=${ARDUINO_MODEL} -lm
CMD_AVR_GCC = avr-gcc ${FLAGS_GCC} ${INCLUDE}
CMD_AVR_GPP = avr-g++ ${FLAGS_GPP} ${INCLUDE}
CMD_AVR_AR = avr-ar rcs

CORE_CPP_SOURCES = $(wildcard ${ARDUINO_CORE_DIR}/*.cpp)
CORE_C_SOURCES = $(wildcard ${ARDUINO_CORE_DIR}/*.c)
COBJECTS=$(CORE_CPP_SOURCES:.cpp=.cpp.o) $(CORE_C_SOURCES:.c=.c.o)
CORE_OBJECTS=$(subst ${ARDUINO_CORE_DIR},${LIB_CORE_DIR},${COBJECTS})

LIB_CPP_SOURCES = $(wildcard $(patsubst %,${ARDUINO_LIB_DIR}/%/*.cpp,$(subst ;, ,$(INCLUDE_LIBS))))
LIB_C_SOURCES = $(wildcard $(patsubst %,${ARDUINO_LIB_DIR}/%/*.c,$(subst ;, ,$(INCLUDE_LIBS))) )
LOBJECTS=$(LIB_CPP_SOURCES:.cpp=.cpp.o) $(LIB_C_SOURCES:.c=.c.o)
LIB_OBJECTS=$(subst ${ARDUINO_LIB_DIR},${LIB_LIBS_DIR},${LOBJECTS})

# Environment 
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin

# build
build: .build-post	
${ARDUINO_LIB_CORE}: ${CORE_OBJECTS}
	mkdir -p $(dir $@)
	${CMD_AVR_AR} ${ARDUINO_LIB_CORE} ${CORE_OBJECTS}
	
${ARDUINO_LIB_LIBS}: ${LIB_OBJECTS}
	mkdir -p $(dir $@)
	${CMD_AVR_AR} ${ARDUINO_LIB_LIBS} ${CORE_OBJECTS} ${LIB_OBJECTS}
	
libraries: ${ARDUINO_LIB_CORE} ${ARDUINO_LIB_LIBS}

${LIB_CORE_DIR}/%.cpp.o: ${ARDUINO_CORE_DIR}/%.cpp
	mkdir -p $(dir $@)
	${CMD_AVR_GPP} $< -o $@

${LIB_CORE_DIR}/%.c.o: ${ARDUINO_CORE_DIR}/%.c
	mkdir -p $(dir $@)
	${CMD_AVR_GCC} $< -o $@

${LIB_LIBS_DIR}/%.cpp.o: ${ARDUINO_LIB_DIR}/%.cpp
	mkdir -p $(dir $@)
	${CMD_AVR_GPP} $< -o $@

${LIB_LIBS_DIR}/%.c.o: ${ARDUINO_LIB_DIR}/%.c
	mkdir -p $(dir $@)
	${CMD_AVR_GCC} $< -o $@

.build-pre: .build-pre-pre libraries

#Unconditional Build
.build-pre-pre:
ifeq "$(wildcard $(ARDUINO_BASE_DIR))" ""
	echo "Folder $(ARDUINO_BASE_DIR) not found. Please fix it on Makefile. Remember the path can not contain spaces."
	test -d ${ARDUINO_BASE_DIR}
endif
	
.build-post: .build-impl	
	avr-objcopy -O ihex ${CND_ARTIFACT_PATH_${CONF}} ${CND_ARTIFACT_PATH_${CONF}}.hex
	avr-size --mcu=${ARDUINO_MODEL} -C ${CND_ARTIFACT_PATH_${CONF}}

#upload: .build-post
upload: 
	${AVR_DUDE} -v -p${ARDUINO_MODEL} -c${ARDUINO_PROGRAMMER} -P ${COM_PORT} -b${BAUD_RATE} -D -Uflash:w:${CND_ARTIFACT_PATH_${CONF}}.hex:i


# clean
clean: .clean-post
	rm -rf dist/Debug/Arduino-Linux/*
	rm -rf ${LIB_CORE_DIR}/*
	rm -rf ${LIB_LIBS_DIR}/*

.clean-pre:
# Add your pre 'clean' code here...

.clean-post: .clean-impl
# Add your post 'clean' code here...


# clobber
clobber: .clobber-post

.clobber-pre:
# Add your pre 'clobber' code here...

.clobber-post: .clobber-impl
# Add your post 'clobber' code here...


# all
all: .all-post

.all-pre:
# Add your pre 'all' code here...

.all-post: .all-impl
# Add your post 'all' code here...


# build tests
build-tests: .build-tests-post

.build-tests-pre:
# Add your pre 'build-tests' code here...

.build-tests-post: .build-tests-impl
# Add your post 'build-tests' code here...


# run tests
test: .test-post

.test-pre: build-tests
# Add your pre 'test' code here...

.test-post: .test-impl
# Add your post 'test' code here...


# help
help: .help-post

.help-pre:
# Add your pre 'help' code here...

.help-post: .help-impl
# Add your post 'help' code here...


# include project implementation makefile
include nbproject/Makefile-impl.mk

# include project make variables
include nbproject/Makefile-variables.mk
