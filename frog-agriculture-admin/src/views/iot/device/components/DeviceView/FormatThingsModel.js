function formatThingsModel(data) {
    //device的数据格式
    console.log('deviceinfo',data)
    let device_info={
        "searchValue": null,
        "createBy": null,
        "createTime": "2023-06-15 10:03:16",
        "updateBy": null,
        "updateTime": "2023-08-02 15:50:11",
        "remark": null,
        "params": {},
        "deviceId": 118,
        "deviceName": "气象站",
        "productId": 99,
        "productName": "气象站",
        "landId": 3,
        "landName": "东22号地块",
        "userId": 1,
        "userName": "admin",
        "tenantId": 1,
        "tenantName": "admin",
        "serialNumber": "test02C00081A5514AB0",
        "firmwareVersion": 1,
        "deviceType": 2,
        "status": 3,
        "rssi": 0,
        "isShadow": 0,
        "networkAddress": " 巴西",
        "networkIp": "177.7.0.1",
        "longitude": 120.146827,
        "latitude": 33.351899,
        "activeTime": "2023-06-15",
        "thingsModelValue": "[{\"id\":\"fengsu\",\"isMonitor\":1,\"name\":\"风速\",\"shadow\":\"133\",\"value\":\"133\"},{\"id\":\"fengxiang\",\"isMonitor\":1,\"name\":\"风向\",\"shadow\":\"南风\",\"value\":\"南风\"},{\"id\":\"wendu\",\"isMonitor\":1,\"name\":\"温度\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"kqsd\",\"isMonitor\":1,\"name\":\"空气湿度\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"kqwd\",\"isMonitor\":1,\"name\":\"空气温度\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"fs\",\"isMonitor\":1,\"name\":\"风速\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"eyhtnd\",\"isMonitor\":1,\"name\":\"二氧化碳浓度\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"ghyxfs\",\"isMonitor\":1,\"name\":\"光合有效辐射\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"jyl\",\"isMonitor\":1,\"name\":\"降雨量\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"gzd\",\"isMonitor\":1,\"name\":\"光照度\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"PM25\",\"isMonitor\":1,\"name\":\"PM2.5\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"rzss\",\"isMonitor\":1,\"name\":\"日照时数\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"fx\",\"isMonitor\":1,\"name\":\"风向\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"pm10\",\"isMonitor\":1,\"name\":\"PM10\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"dqyl\",\"isMonitor\":1,\"name\":\"大气压力\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"zfs\",\"isMonitor\":1,\"name\":\"总辐射\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"pm25\",\"isMonitor\":1,\"name\":\"PM2.5\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"switch\",\"isMonitor\":0,\"name\":\"设备开关\",\"shadow\":\"\",\"value\":\"\"},{\"id\":\"gear\",\"isMonitor\":0,\"name\":\"运行档位\",\"shadow\":\"\",\"value\":\"\"}]",
        "imgUrl": "/profile/iot/1/2023-0728-094505.png,/profile/iot/1/2023-0728-100209.svg",
        "locationWay": 3,
        "summary": null,
        "groupId": null,
        "isOwner": null,
        "delFlag": null,
        "cacheThingsModel": {
            "functions": [
                {
                    "isReadonly": 0,
                    "datatype": {
                        "enumList": [
                            {
                                "text": "低速档位",
                                "value": "0"
                            },
                            {
                                "text": "中速档位",
                                "value": "1"
                            },
                            {
                                "text": "中高速档位",
                                "value": "2"
                            },
                            {
                                "text": "高速档位",
                                "value": "3"
                            }
                        ],
                        "type": "enum"
                    },
                    "isTop": 1,
                    "name": "运行档位",
                    "id": "gear",
                    "order": 0
                },
                {
                    "isReadonly": 0,
                    "datatype": {
                        "trueText": "打开",
                        "falseText": "关闭",
                        "type": "bool"
                    },
                    "isTop": 1,
                    "name": "设备开关",
                    "id": "switch",
                    "order": 0
                }
            ],
            "events": [
                {
                    "datatype": {
                        "unit": "℃",
                        "min": 0,
                        "max": 100,
                        "step": 0.1,
                        "type": "decimal"
                    },
                    "name": "环境温度过高",
                    "id": "height_temperature"
                },
                {
                    "datatype": {
                        "type": "string",
                        "maxLength": 1024
                    },
                    "name": "设备发生异常",
                    "id": "exception"
                }
            ],
            "properties": [
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "hPa",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "大气压力",
                    "id": "dqyl",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "mm",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "降雨量",
                    "id": "jyl",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "ppm",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "二氧化碳浓度",
                    "id": "eyhtnd",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "ug/m³",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "PM10",
                    "id": "pm10",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "ug/m³",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "PM2.5",
                    "id": "pm25",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "Lux",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "光照度",
                    "id": "gzd",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "%",
                        "min": 1,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "空气湿度",
                    "id": "kqsd",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "W/㎡",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "总辐射",
                    "id": "zfs",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "W/㎡",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "光合有效辐射",
                    "id": "ghyxfs",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "h",
                        "min": 1,
                        "max": 24,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "日照时数",
                    "id": "rzss",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "°C",
                        "min": 1,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "空气温度",
                    "id": "kqwd",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "",
                        "min": 1,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "风向",
                    "id": "fx",
                    "order": 0
                },
                {
                    "isMonitor": 1,
                    "isReadonly": 1,
                    "datatype": {
                        "unit": "m/s",
                        "min": 0,
                        "max": 100,
                        "step": 1,
                        "type": "integer"
                    },
                    "isTop": 1,
                    "name": "风速",
                    "id": "fs",
                    "order": 0
                }
            ]
        },
        "thingsModels": [
            {
                "id": "gear",
                "name": "运行档位",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 0,
                "isReadonly": 0,
                "type": 2,
                "order": 0,
                "dataType": {
                    "type": "enum",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": [
                        {
                            "text": "低速档位",
                            "value": "0"
                        },
                        {
                            "text": "中速档位",
                            "value": "1"
                        },
                        {
                            "text": "中高速档位",
                            "value": "2"
                        },
                        {
                            "text": "高速档位",
                            "value": "3"
                        }
                    ],
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "switch",
                "name": "设备开关",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 0,
                "isReadonly": 0,
                "type": 2,
                "order": 0,
                "dataType": {
                    "type": "bool",
                    "falseText": "关闭",
                    "trueText": "打开",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            }
        ],
        "monitorList": [
            {
                "id": "dqyl",
                "name": "大气压力",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "hPa",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "jyl",
                "name": "降雨量",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "mm",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "eyhtnd",
                "name": "二氧化碳浓度",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "ppm",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "pm10",
                "name": "PM10",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "ug/m³",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "pm25",
                "name": "PM2.5",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "ug/m³",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "gzd",
                "name": "光照度",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "Lux",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "kqsd",
                "name": "空气湿度",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 1,
                    "max": 100,
                    "step": 1,
                    "unit": "%",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "zfs",
                "name": "总辐射",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "W/㎡",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "ghyxfs",
                "name": "光合有效辐射",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "W/㎡",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "rzss",
                "name": "日照时数",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 1,
                    "max": 24,
                    "step": 1,
                    "unit": "h",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "kqwd",
                "name": "空气温度",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 1,
                    "max": 100,
                    "step": 1,
                    "unit": "°C",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "fx",
                "name": "风向",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 1,
                    "max": 100,
                    "step": 1,
                    "unit": "",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            },
            {
                "id": "fs",
                "name": "风速",
                "value": "",
                "shadow": "",
                "isTop": 1,
                "isMonitor": 1,
                "isReadonly": 1,
                "type": 1,
                "order": 0,
                "dataType": {
                    "type": "integer",
                    "falseText": "",
                    "trueText": "",
                    "min": 0,
                    "max": 100,
                    "step": 1,
                    "unit": "m/s",
                    "arrayType": "",
                    "arrayCount": 0,
                    "maxLength": 1024,
                    "enumList": null,
                    "params": null,
                    "arrayParams": null
                }
            }
        ]
    }
    data.monitorList = [];
    // 物模型格式化
    for (let i = 0; i < data.thingsModels.length; i++) {
        if (data.thingsModels[i].dataType.type == "array") {
            if (data.thingsModels[i].dataType.arrayType == "object") {
                for (let k = 0; k < data.thingsModels[i].dataType.arrayParams.length; k++) {
                    for (let j = 0; j < data.thingsModels[i].dataType.arrayParams[k].length; j++) {
                        // 数组元素中参数ID添加前缀，例如：array_00_
                        let index = k > 9 ? String(k) : '0' + k;
                        let prefix = 'array_' + index + '_';
                        data.thingsModels[i].dataType.arrayParams[k][j].id = prefix + data.thingsModels[i].dataType.arrayParams[k][j].id;
                        // 图表分类放置
                        if (data.thingsModels[i].dataType.arrayParams[k][j].isMonitor == 1) {
                            data.thingsModels[i].dataType.arrayParams[k][j].name = "[" + data.thingsModels[i].name + (k + 1) + "] " + data.thingsModels[i].dataType.arrayParams[k][j].name;
                            data.thingsModels[i].dataType.arrayParams[k][j].dataType.arrayType = "object";
                            data.monitorList.push(data.thingsModels[i].dataType.arrayParams[k][j]);
                            data.thingsModels[i].dataType.arrayParams[k].splice(j--, 1);
                        }
                    }
                }
            } else {
                // 字符串拆分为物模型数组 model=id/name/type/isReadonly/value/shadow
                let values = data.thingsModels[i].value != "" ? data.thingsModels[i].value.split(',') : [];
                let shadows = data.thingsModels[i].shadow != "" ? data.thingsModels[i].shadow.split(',') : [];
                for (let j = 0; j < data.thingsModels[i].dataType.arrayCount; j++) {
                    if (!data.thingsModels[i].dataType.arrayModel) {
                        data.thingsModels[i].dataType.arrayModel = [];
                    }
                    // 数组里面的ID需要添加前缀和索引，例如：array_00_temperature
                    let index = j > 9 ? String(j) : '0' + j;
                    let prefix = 'array_' + index + '_';
                    data.thingsModels[i].dataType.arrayModel[j] = {
                        id: prefix + data.thingsModels[i].id,
                        name: data.thingsModels[i].name,
                        type: data.thingsModels[i].type,
                        isReadonly: data.thingsModels[i].isReadonly,
                        value: values[j] ? values[j] : "",
                        shadow: shadows[j] ? shadows[j] : ""
                    }
                }

            }
        } else if (data.thingsModels[i].dataType.type == "object") {
            for (let j = 0; j < data.thingsModels[i].dataType.params.length; j++) {
                // 图表分类放置
                if (data.thingsModels[i].dataType.params[j].isMonitor == 1) {
                    data.thingsModels[i].dataType.params[j].name = "[" + data.thingsModels[i].name + "] " + data.thingsModels[i].dataType.params[j].name;
                    data.monitorList.push(data.thingsModels[i].dataType.params[j]);
                    data.thingsModels[i].dataType.params.splice(j--, 1);
                }
            }
        } else if (data.thingsModels[i].isMonitor == 1) {
            data.monitorList.push(data.thingsModels[i]);
            // 使用i--解决索引变更问题
            data.thingsModels.splice(i--, 1);
        }

    }
}
export { formatThingsModel }
