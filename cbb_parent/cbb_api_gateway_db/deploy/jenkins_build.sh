#!/bin/sh -l
set -e
#项目名称
projectName="cbb_api_gateway_db"
#组件名称
component_name="API网关组件"
#组件描述
component_description="API网关组件"
#组件标签
component_tag=${projectName}

source ${WORKSPACE}/cbb_parent/env/env.sh

WebServer_Port=${ApiGateway_Port}

source ${WORKSPACE}/cbb_parent/env/build_wrapper.sh
