# WSL Path Converter

![Build](https://github.com/Alanxtl/wsl_path_converter/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/27636.svg)](https://plugins.jetbrains.com/plugin/27636)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/27636.svg)](https://plugins.jetbrains.com/plugin/27636)

A simple plugin for JetBrains IDEs designed to solve a common pain point when developing with WSL (Windows Subsystem for Linux) on Windows: path conversion.
<!-- Plugin description -->
Report issues on the [GitHub repository](https://github.com/Alanxtl/wsl_path_converter).

This plugin allows you to copy the WSL-style path of any file in your project with a single click, saving you the hassle of manual conversion.

Make sure you have put `wsl.exe` in your system variable `PATH`.

此插件可以让你一键复制项目中任何文件的 WSL 格式路径，省去手动转换的麻烦。

请确保 `wsl.exe` 已经被加入到系统 `PATH` 环境变量当中。
<!-- Plugin description end -->

-----

## Prerequisite

Make sure you have put `wsl.exe` in your system path.

-----

## 🚀 Features

* **One-Click Copy**: Adds a "Copy Path in WSL" option to the "Copy Path/Reference..." menu.
* **Accurate and Reliable**: Directly calls the `wsl.exe wslpath` command for conversion, ensuring the result is perfectly consistent with your local WSL configuration, including support for custom mount points.
* **Zero Configuration**: Works out of the box with no additional setup required.
* **Broad Compatibility**: Supports all major JetBrains IDEs (IntelliJ IDEA, PyCharm, WebStorm, GoLand, Android Studio, etc.).

-----

一个为 JetBrains IDE 设计的简单插件，旨在解决在 Windows 上进行 WSL (Windows Subsystem for Linux) 开发时的一个常见痛点：路径转换。

此插件可以让你一键复制项目中任何文件的 WSL 格式路径，省去手动转换的麻烦。

-----

## 🚀 功能特性

* **一键复制**：在“Copy Path/Reference...”菜单中增加一个 “Copy Path in WSL” 选项。
* **精准可靠**：直接调用 `wsl.exe wslpath` 命令进行转换，确保结果与您本机的 WSL 配置完全一致，支持自定义挂载点。
* **零配置**：安装即用，无需任何额外配置。
* **广泛兼容**：支持所有主流的 JetBrains IDEs (IntelliJ IDEA, PyCharm, WebStorm, GoLand, Android Studio 等)。

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "wsl_path_converter"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27636) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/27636/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/Alanxtl/wsl_path_converter/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation


