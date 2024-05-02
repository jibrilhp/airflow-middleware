#!/usr/bin/env bash
#
CONFIG_NAME="application.yml"

## PACKSCRIPT-INCLUDE .version.sh .inc.sh
SRCDIR=$(cd $(dirname $0) && pwd) && source ${SRCDIR}/.version.sh && source ${SRCDIR}/.inc.sh

DRY_RUN="NO"
showHelp() {
    echoerr "Usage: $0 [options]"
    echoerr ""
    echoerr "Options:"
    echoerr " -d  : dry-run mode"
    echoerr " -H  : show this help"
    echoerr ""
}

while getopts ":dH" opt; do
  case ${opt} in
  d)
    DRY_RUN="YES"
    ;;
  H)
    showHelp
    exit 0
    ;;
  esac
done

## PACKSCRIPT-ATTACH JARFILE cli/target/cli-${OUTPUT_VERSION}.jar
JARFILE="${BASEDIR}/cli/target/cli-${APP_VERSION}.jar"

if [[ "${DRY_RUN}" == "YES" ]]; then
  echo "${JAVABIN} -Dapp.home=${WORKING_DIR} -Dmicronaut.env.deduction=false -Dmicronaut.config.files=${APPCONFIG} -jar ${JARFILE}" "$@"
else
  cd ${WORKING_DIR} && ${JAVABIN} -Dapp.home=${WORKING_DIR} -Dorg.jooq.no-tips=true -Dorg.jooq.no-logo=true -Dmicronaut.env.deduction=false -Dmicronaut.config.files=${APPCONFIG} -jar ${JARFILE} "$@"
fi
