<?xml version="1.0"?>
<globals>
    <global id="hasNoActionBar" type="boolean" value="false" />
    <global id="parentActivityClass" value="" />
    <global id="simpleLayoutName" value="${layoutName}" />
    <global id="excludeMenu" type="boolean" value="true" />
    <global id="generateActivityTitle" type="boolean" value="false" />
    <global id="firstActivityName" type="string" value="${activityClass?substring(0,activityClass?index_of("Activity"))}" />
    <#include "../common/common_globals.xml.ftl" />
</globals>

