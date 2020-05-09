<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
    <#include "../common/recipe_manifest.xml.ftl" />
    <@kt.addAllKotlinDependencies />

<#if generateLayout>；
    <instantiate from="root/res/layout/simple.xml.ftl"
                 to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
    <open file="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
</#if>

	<!--View-activity-->
    <instantiate from="root/src/app_package/MvpView.${ktOrJavaExt}.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${firstActivityName}View.${ktOrJavaExt}" />
	<!--Model-->
	<instantiate from="root/src/app_package/MvpModel.${ktOrJavaExt}.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${firstActivityName}Model.${ktOrJavaExt}" />	
	<!--Presenter-->
	<instantiate from="root/src/app_package/MvpActivity.${ktOrJavaExt}.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${firstActivityName}Activity.${ktOrJavaExt}" />	
    <open file="${escapeXmlAttribute(srcOut)}/${activityClass}.${ktOrJavaExt}" />

</recipe>
