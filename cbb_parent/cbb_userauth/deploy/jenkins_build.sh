#!/bin/sh -l
set -e
#项目名称
projectName="cbb_userauth"
#组件名称
component_name="用户,角色,菜单管理组件"
#组件描述
component_description="用户,角色,菜单管理组件"
#组件标签
component_tag=${projectName}



source ${WORKSPACE}/cbb_parent/env/env.sh

WebServer_Port=${UserAuth_Port}

source ${WORKSPACE}/cbb_parent/env/build_wrapper.sh
