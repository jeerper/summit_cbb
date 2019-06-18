#!/bin/sh -l
set -e
#项目名称
projectName="cbb_weather"
#组件名称
component_name="天气组件"
#组件描述
component_description="天气组件"
#组件标签
component_tag=${projectName}

source ${WORKSPACE}/cbb_parent/env/env.sh

source ${WORKSPACE}/cbb_parent/env/build_wrapper.sh
