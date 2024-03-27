package com.eduardo.customerregistration

import androidx.test.espresso.device.filter.RequiresDisplay
import androidx.test.espresso.device.sizeclass.HeightSizeClass
import androidx.test.espresso.device.sizeclass.WidthSizeClass
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/*
 * Illustrates usage of @RequiresDisplay API to filter tests based on display attributes such a screen size.
 */
@RunWith(AndroidJUnit4::class)
class RequiresDisplayTest {
    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.COMPACT,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.COMPACT
    )
    @Test
    fun testOnDevicesWithCompactWidthAndHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.COMPACT,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.MEDIUM
    )
    @Test
    fun testOnDevicesWithCompactWidthAndMediumHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.MEDIUM,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.COMPACT
    )
    @Test
    fun testOnDevicesWithMediumWidthAndCompactHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.COMPACT,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.EXPANDED
    )
    @Test
    fun testOnDevicesWithCompactWidthAndExpandedHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.EXPANDED,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.COMPACT
    )
    @Test
    fun testOnDevicesWithExpandedWidthAndCompactHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.MEDIUM,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.MEDIUM
    )
    @Test
    fun testOnDevicesWithMediumWidthAndHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.EXPANDED,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.MEDIUM,
    )
    @Test
    fun testOnDevicesWithExpandedWidthAndMediumHeight() {}


    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.MEDIUM,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.EXPANDED
    )
    @Test
    fun testOnDevicesWithMediumWidthAndExpandedHeight() {}

    @RequiresDisplay(
        widthSizeClass = WidthSizeClass.Companion.WidthSizeClassEnum.EXPANDED,
        heightSizeClass = HeightSizeClass.Companion.HeightSizeClassEnum.EXPANDED
    )
    @Test
    fun testOnDevicesWithExpandedWidthAndHeight() {}
}