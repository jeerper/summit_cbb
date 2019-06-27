#!/bin/sh -l
set -e
#项目名称
projectName="cbb_notification"
#组件名称
component_name="消息通知组件，"
#组件描述
component_description="消息通知组件"
#组件标签
component_tag=${projectName}

source ${WORKSPACE}/cbb_parent/env/env.sh

WebServer_Port=${Demo_Port}

source ${WORKSPACE}/cbb_parent/env/build_wrapper.sh

