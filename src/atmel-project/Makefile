
# Environment AVR
AVR_DUDE = avrdude

FLAGS_GCC = -Os -Wall -ffunction-sections -fdata-sections -fpack-struct -mmcu=${MMC_MODEL} -MMD
FLAGS_GPP = ${FLAGS_GCC} -fno-exceptions
FLAGS_LINKER = -Os -Wl,--gc-sections,--relax -mmcu=${MMC_MODEL} -lm

# Environment 
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin

# build
build: .build-post

.build-pre:
# Add your pre 'build' code here...

.build-post: .build-impl 
	avr-objcopy -O ihex ${CND_ARTIFACT_PATH_${CONF}} ${CND_ARTIFACT_PATH_${CONF}}.hex
	avr-size --mcu=${MMC_MODEL} -C ${CND_ARTIFACT_PATH_${CONF}}
# Add your post 'build' code here...

upload:
	${AVR_DUDE} -v -p${MMC_MODEL_AVRDURE} -c${PROGRAMMER} -P${PORT} -Uflash:w:${CND_ARTIFACT_PATH_${CONF}}.hex:i

# clean
clean: .clean-post

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
