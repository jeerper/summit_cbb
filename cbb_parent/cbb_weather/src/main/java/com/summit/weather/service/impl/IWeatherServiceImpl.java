package com.summit.weather.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summit.weather.dto.OpWeatherReal;
import com.summit.weather.repository.OpWeatherRealRepository;
import com.summit.weather.service.IWeatherService;
import com.summit.weather.util.CommonUtil;
import com.summit.weather.util.EncodUtils;
import com.summit.weather.util.HttpRequestUtils;
import com.summit.weather.util.TimeUtil;
import com.summit.weather.vo.WeatherForecastVO;
import com.summit.weather.vo.WeatherForecastVO.FutureWeather;
import com.summit.weather.vo.WeatherForecastVO.RealWeather;
import com.summit.weather.vo.WeatherForecastVO.TodayWeather;
import com.summit.weather.vo.WeatherHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class IWeatherServiceImpl implements IWeatherService {

    private static final Logger log = LoggerFactory.getLogger(IWeatherServiceImpl.class);

    /**
     * 需要记录历史天气的城市名称
     */
    @Value("${weather.city.name}")
    String cityName;

    @Value("${weather.key.ibs}")
    String ibsKey;

    @Value("${weather.key.juhe}")
    String juheKey;

    @Value("${weather.key.seniverse}")
    String seniverseKey;

    @Autowired
    OpWeatherRealRepository opWeatherRealRepository;

    /**
     * 加载中国气象网城市代码
     */
    static Map<String, String> chinaCityCodeMap = new HashMap<>();

    static {
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        Class<IWeatherServiceImpl> clazz = IWeatherServiceImpl.class;
        try {
            InputStream inputestream = clazz.getResourceAsStream("/CityCode.properties");
            properties.load(new InputStreamReader(inputestream, "UTF-8"));
            // 获取key对应的value值
            Enumeration<?> en = properties.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = properties.getProperty(key);
                chinaCityCodeMap.put(key, property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WeatherForecastVO getWeatherForecast(String lgtd, String lttd) {

        WeatherForecastVO weatherForecastVO = null;

        if (weatherForecastVO == null) {
            // 高德地图API
            weatherForecastVO = findIbsWeartherByLgtdAndLttd(lgtd, lttd);
        }
        if (weatherForecastVO == null) {
            // 中国天气网
            weatherForecastVO = findChinaWeartherByLgtdAndLttd(lgtd, lttd);
        }
        if (weatherForecastVO == null) {
            // 心知天气网
            weatherForecastVO = findSeniverseByLgtdAndLttd(lgtd, lttd);
        }
        if (weatherForecastVO == null) {
            // 聚合数据
            weatherForecastVO = findJuheWeartherByLgtdAndLttd(lgtd, lttd);
        }

        return weatherForecastVO;
    }

    @Override
    public WeatherForecastVO getWeatherForecast(String cityName) {
        WeatherForecastVO WeatherForecastVO = null;

        if (WeatherForecastVO == null) {
            // 高德地图API
            WeatherForecastVO = findIbsWeartherByCityName(cityName);
        }
        if (WeatherForecastVO == null) {
            // 中国天气网
            WeatherForecastVO = findChinaWeartherByCityName(cityName);
        }
        if (WeatherForecastVO == null) {
            // 心知天气网
            WeatherForecastVO = findSeniverseByCityName(cityName);
        }
        if (WeatherForecastVO == null) {
            // 聚合数据
            WeatherForecastVO = findJuheWeartherByCityName(cityName);
        }

        return WeatherForecastVO;
    }

    @Override
    public String save() {
        String[] cityNameArray = cityName.split(";");

        for (String cityName : cityNameArray) {
            WeatherForecastVO WeatherForecastVO = this.findIbsWeartherByCityName(cityName);
            if (WeatherForecastVO != null) {
                OpWeatherReal opWeatherReal = new OpWeatherReal();
                opWeatherReal.setAddress(WeatherForecastVO.getTodayWeather().getCity());
                opWeatherReal.setTemperature(WeatherForecastVO.getTodayWeather().getTemperature());
                opWeatherReal.setWeather(WeatherForecastVO.getTodayWeather().getWeather());
                opWeatherReal.setWind(WeatherForecastVO.getTodayWeather().getWind());
                opWeatherRealRepository.save(opWeatherReal);
            }
        }
        return "SUCCESS";
    }

    @Override
    public List<WeatherHistoryVO> getWeatherHistory(String cityName, Date startTime, Date endTime) {
        List<WeatherHistoryVO> historyVO = null;
        Sort sort = new Sort(Direction.DESC, "moditime");
        List<OpWeatherReal> weatherReals = opWeatherRealRepository
                .findAll(this.getWhereClause(cityName, startTime, endTime), sort);

        if (weatherReals != null && weatherReals.size() > 0) {
            historyVO = new LinkedList<>();
            for (OpWeatherReal opWeatherReal : weatherReals) {
                WeatherHistoryVO weatherHistoryVo = new WeatherHistoryVO();
                weatherHistoryVo.setCityName(opWeatherReal.getAddress());
                weatherHistoryVo.setTime(opWeatherReal.getModitime());
                weatherHistoryVo.setTemperature(opWeatherReal.getTemperature());
                weatherHistoryVo.setWeather(opWeatherReal.getWeather());
                weatherHistoryVo.setWind(opWeatherReal.getWind());
                historyVO.add(weatherHistoryVo);
            }
        }
        return historyVO;
    }

    private Specification<OpWeatherReal> getWhereClause(String address, Date startTime, Date endTime) {
        return new Specification<OpWeatherReal>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<OpWeatherReal> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {

                Predicate predicate = criteriaBuilder.conjunction();

                if (!StringUtils.isEmpty(predicate)) {
                    predicate.getExpressions()
                            .add((criteriaBuilder.like(root.<String>get("address"), "%" + address + "%")));
                }
                // 大于等于开始时间
                if (startTime != null) {
                    predicate.getExpressions()
                            .add((criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("moditime"), startTime)));
                }
                // 小于等于结束时间
                if (endTime != null) {
                    predicate.getExpressions()
                            .add((criteriaBuilder.lessThanOrEqualTo(root.<Date>get("moditime"), endTime)));
                }

                return predicate;
            }

        };
    }

    /**
     * 高德地图API根据经纬度查询天气预报信息
     *
     * @param lgtd
     * @param lttd
     * @return
     */
    private WeatherForecastVO findIbsWeartherByLgtdAndLttd(String lgtd, String lttd) {
        WeatherForecastVO WeatherForecastVO = null;

        try {
            // 先使用经纬度转换成城市代码，
            WeatherForecastVO = new WeatherForecastVO();
            String findCityUrl = "https://restapi.amap.com/v3/geocode/regeo";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("location", lgtd + "," + lttd);
            paramMap.put("key", ibsKey);
            paramMap.put("output", "JSON");
            String cityString = HttpRequestUtils.post(findCityUrl, paramMap);
            JSONObject cityJson = JSON.parseObject(cityString);

            // 根据城市代码查询天气情况
            String regeocode = cityJson.getJSONObject("regeocode").getJSONObject("addressComponent")
                    .getString("adcode");
            WeatherForecastVO = findIbsWeartherByCode(regeocode);

        } catch (Exception e) {
            WeatherForecastVO = null;
            e.printStackTrace();
        }
        return WeatherForecastVO;
    }

    /**
     * 高德地图API根据城市代码，查询天气信息
     *
     * @param regeocode
     * @return
     * @throws Exception
     */
    private WeatherForecastVO findIbsWeartherByCode(String regeocode) throws Exception {
        WeatherForecastVO WeatherForecastVO = new WeatherForecastVO();

        String weatherUrl = "https://restapi.amap.com/v3/weather/weatherInfo";
        Map<String, String> paramMap = new HashMap<>();

        paramMap = new HashMap<>();
        paramMap.put("city", regeocode);
        paramMap.put("key", ibsKey);
        paramMap.put("extensions", "all");

        // 整合当天天气状况
        String weaherString = HttpRequestUtils.get(weatherUrl, paramMap);
        JSONObject weaherJson = JSON.parseObject(weaherString);
        log.info("weaherString:" + weaherString);

        JSONObject forecasts = weaherJson.getJSONArray("forecasts").getJSONObject(0);
        JSONArray casts = forecasts.getJSONArray("casts");
        TodayWeather todayWeather = new TodayWeather();
        todayWeather.setCity(forecasts.getString("city"));
        todayWeather.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        todayWeather.setDressingAdvice("暂无数据");
        todayWeather.setTemperature(((JSONObject) casts.get(0)).getString("nighttemp") + "~"
                + ((JSONObject) casts.get(0)).getString("daytemp") + "℃");
        todayWeather.setUvIndex("暂无数据");
        todayWeather.setWeather(((JSONObject) casts.get(0)).getString("dayweather"));
        todayWeather.setWeek(TimeUtil.getWeek(new Date()));
        todayWeather.setWind(((JSONObject) casts.get(0)).getString("daypower"));

        // 整合未来天气状况
        List<FutureWeather> futureWeathers = new LinkedList<>();
        for (int i = 1; i < casts.size(); i++) {
            FutureWeather futureWeather = new FutureWeather();
            JSONObject tmp = (JSONObject) casts.get(i);

            futureWeather.setDate(new SimpleDateFormat("yyyy-MM-dd").format(TimeUtil.addDay(new Date(), i)));
            futureWeather.setTemperature((tmp).getString("nighttemp") + "~" + (tmp).getString("daytemp")+ "℃");
            futureWeather.setWeather((tmp).getString("dayweather"));
            futureWeather.setWeek(TimeUtil.getWeek(TimeUtil.addDay(new Date(), i)));
            futureWeather.setWind(tmp.getString("daypower"));
            futureWeathers.add(futureWeather);
        }

        // 整合实时天气状况
        RealWeather realWeather = new RealWeather();
        weatherUrl = "https://restapi.amap.com/v3/weather/weatherInfo";
        paramMap = new HashMap<>();
        paramMap.put("city", regeocode);
        paramMap.put("key", ibsKey);
        paramMap.put("extensions", "base");
        String realWeaherString = HttpRequestUtils.get(weatherUrl, paramMap);
        JSONObject realWeaherJson = ((JSONObject) (JSONObject.parseObject(realWeaherString).getJSONArray("lives")
                .get(0)));

        realWeather.setDate(realWeaherJson.getString("reporttime"));
        realWeather.setTemperature(realWeaherJson.getString("temperature") + "℃");
        realWeather.setWeather(realWeaherJson.getString("weather"));
        realWeather.setWind(realWeaherJson.getString("windpower"));

        paramMap = new HashMap<>();
        paramMap.put("city", regeocode);
        paramMap.put("key", ibsKey);
        paramMap.put("extensions", "base");

        WeatherForecastVO.setFutureWeathers(futureWeathers);
        WeatherForecastVO.setTodayWeather(todayWeather);
        WeatherForecastVO.setRealWeather(realWeather);

        return WeatherForecastVO;
    }

    private WeatherForecastVO findChinaWeartherByLgtdAndLttd(String lgtd, String lttd) {
        WeatherForecastVO WeatherForecastVO = null;

        try {
            WeatherForecastVO = new WeatherForecastVO();
            // 根据经纬度转换城市名
            String weatherUrl = "http://gc.ditu.aliyun.com/regeocoding";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("l", lttd + "," + lgtd);
            paramMap.put("type", "111");
            String cityString = HttpRequestUtils.get(weatherUrl, paramMap);
            JSONObject cityJson = JSON.parseObject(cityString);
            JSONArray cityArray = cityJson.getJSONArray("addrList");
            cityJson = (JSONObject) cityArray.get(0);
            String[] addressName = cityJson.getString("admName").split(",");

            String cityCode = null;
            // 根据地名转换成城市代码
            for (int i = addressName.length - 1; i >= 0; i--) {
                cityName = addressName[i];
                cityCode = chinaCityCodeMap.get(addressName[i]);
                if (StringUtils.isEmpty(cityCode)) {
                    cityCode = chinaCityCodeMap.get(replaceStr(addressName[i]));
                    cityName = replaceStr(addressName[i]);
                }
                if (!StringUtils.isEmpty(cityCode)) {
                    break;
                }
            }
            if (StringUtils.isEmpty(cityCode)) {
                return null;
            }
            // 根据城市代码查询天气状况
            weatherUrl = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + cityCode;
            String weatherString = HttpRequestUtils.unzipGet(weatherUrl, null);
            JSONObject weatherJson = JSON.parseObject(weatherString);

            // 整合今天天气数据
            JSONObject data = weatherJson.getJSONObject("data");
            JSONArray forecast = data.getJSONArray("forecast");
            TodayWeather todayWeather = new TodayWeather();
            todayWeather.setCity(data.getString("city"));
            todayWeather.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            todayWeather.setDressingAdvice(data.getString("ganmao"));
            todayWeather.setTemperature(((JSONObject) forecast.get(0)).getString("low") + "~"
                    + ((JSONObject) forecast.get(0)).getString("high")+"℃");
            todayWeather.setUvIndex("暂无数据");
            todayWeather.setWeather(((JSONObject) forecast.get(0)).getString("type"));
            todayWeather.setWeek(TimeUtil.getWeek(new Date()));
            todayWeather.setWind(CommonUtil.getCdata(((JSONObject) forecast.get(0)).getString("fengli")));

            List<FutureWeather> futureWeathers = new LinkedList<>();
            for (int i = 0; i < forecast.size(); i++) {
                FutureWeather futureWeather = new FutureWeather();
                JSONObject tmp = (JSONObject) forecast.get(i);

                futureWeather.setDate(new SimpleDateFormat("yyyy-MM-dd").format(TimeUtil.addDay(new Date(), i)));
                futureWeather.setTemperature((tmp).getString("low") + "~" + (tmp).getString("high")+"℃");
                futureWeather.setWeather((tmp).getString("type"));
                futureWeather.setWeek(TimeUtil.getWeek(TimeUtil.addDay(new Date(), i)));
                futureWeather.setWind(CommonUtil.getCdata(tmp.getString("fengli")));
                futureWeathers.add(futureWeather);
            }

            WeatherForecastVO.setFutureWeathers(futureWeathers);
            WeatherForecastVO.setTodayWeather(todayWeather);
        } catch (Exception e) {
            WeatherForecastVO = null;
            e.printStackTrace();
        }
        return WeatherForecastVO;
    }

    /**
     * 高德地图API根据城市名查询天气信息
     *
     * @param cityName
     * @return
     */
    private WeatherForecastVO findIbsWeartherByCityName(String cityName) {
        WeatherForecastVO WeatherForecastVO = null;
        try {
            if (!EncodUtils.isUTF8(cityName)) {
                // 对String进行转码
                cityName = new String(cityName.getBytes(), "UTF-8");
            }

            // 查询高德地图城市编号
            String findCityUrl = "https://restapi.amap.com/v3/geocode/geo";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("address", cityName);
            paramMap.put("city", cityName);
            paramMap.put("key", ibsKey);
            paramMap.put("output", "JSON");
            String cityString = HttpRequestUtils.post(findCityUrl, paramMap);
            JSONObject cityJson = JSON.parseObject(cityString);
            log.info(cityJson.toJSONString());
            String cityCode = ((JSONObject) (cityJson.getJSONArray("geocodes").get(0))).getString("adcode");
            WeatherForecastVO = findIbsWeartherByCode(cityCode);
        } catch (Exception e) {
            WeatherForecastVO = null;
            e.printStackTrace();
        }
        return WeatherForecastVO;
    }

    private static String replaceStr(String cityName) {
        cityName = cityName.replace("市", "");
        cityName = cityName.replace("县", "");
        cityName = cityName.replace("区", "");
        return cityName;
    }

    private WeatherForecastVO findSeniverseByLgtdAndLttd(String lgtd, String lttd) {

        WeatherForecastVO WeatherForecastVO = null;

        try {

            // 根据经纬度查询天气
            String url = "https://api.seniverse.com/v3/weather/daily.json";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("key", seniverseKey);
            paramMap.put("location", lttd + ":" + lgtd);
            paramMap.put("language", "zh-Hans");
            paramMap.put("c", "zh-Hans");
            paramMap.put("start", "0");
            paramMap.put("days", "5");
            String weatherString = HttpRequestUtils.get(url, paramMap);
            JSONObject weatherJson = JSON.parseObject(weatherString);

            JSONObject results = weatherJson.getJSONArray("results").getJSONObject(0);
            JSONObject location = results.getJSONObject("location");
            JSONObject todayWeatherJson = results.getJSONArray("daily").getJSONObject(0);
            JSONArray futureWeathersJson = results.getJSONArray("daily");

            // 整合当天天气状况
            TodayWeather todayWeather = new TodayWeather();
            todayWeather.setCity(location.getString("name"));
            todayWeather.setDate(todayWeatherJson.getString("date"));
            todayWeather.setDressingAdvice("暂无数据");
            todayWeather.setTemperature(todayWeatherJson.getString("low") + "~" + todayWeatherJson.getString("high"));
            todayWeather.setUvIndex("暂无数据");
            todayWeather.setWeather(todayWeatherJson.getString("text_day"));
            todayWeather.setWeek(TimeUtil.getWeek(new Date()));
            todayWeather
                    .setWind(todayWeatherJson.getString("wind_scale") + " " + todayWeatherJson.getString("wind_speed"));

            // 整合未来天气状况
            List<FutureWeather> futureWeathers = new LinkedList<>();
            for (int i = 0; i < futureWeathersJson.size(); i++) {
                FutureWeather futureWeather = new FutureWeather();
                JSONObject tmp = (JSONObject) futureWeathersJson.get(i);
                futureWeather.setDate(new SimpleDateFormat("yyyy-MM-dd").format(TimeUtil.addDay(new Date(), i)));
                futureWeather.setTemperature(tmp.getString("low") + "~" + tmp.getString("high"));
                futureWeather.setWeather(tmp.getString("text_day"));
                futureWeather.setWeek(TimeUtil.getWeek(TimeUtil.addDay(new Date(), i)));
                futureWeather.setWind(tmp.getString("wind_scale") + " " + tmp.getString("wind_speed"));
                futureWeathers.add(futureWeather);
            }
            WeatherForecastVO.setFutureWeathers(futureWeathers);
            WeatherForecastVO.setTodayWeather(todayWeather);
        } catch (Exception e) {
            WeatherForecastVO = null;
            e.printStackTrace();
        }
        return WeatherForecastVO;
    }

    private WeatherForecastVO findJuheWeartherByLgtdAndLttd(String lgtd, String lttd) {
        WeatherForecastVO WeatherForecastVO = null;

        try {
            String weatherUrl = "http://v.juhe.cn/weather/geo";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("lon", lgtd);
            paramMap.put("lat", lttd);
            paramMap.put("format", "2");
            paramMap.put("dtype", "json");
            paramMap.put("key", juheKey);

            JSONObject result = JSONObject.parseObject(HttpRequestUtils.post(weatherUrl, paramMap));
            if ("200".equals(result.getString("resultcode"))) {
                WeatherForecastVO = new WeatherForecastVO();

                JSONObject resultJson = result.getJSONObject("result");

                JSONObject todayJson = resultJson.getJSONObject("today");

                // 整合今天天气数据
                TodayWeather todayWeather = new TodayWeather();
                todayWeather.setCity(todayJson.getString("city"));
                todayWeather.setDate(todayJson.getString("date_y"));
                todayWeather.setDressingAdvice(todayJson.getString("dressing_advice"));
                todayWeather.setTemperature(todayJson.getString("temperature"));
                todayWeather.setUvIndex(todayJson.getString("uv_index"));
                todayWeather.setWeather(todayJson.getString("weather"));
                todayWeather.setWeek(todayJson.getString("week"));
                todayWeather.setWind(todayJson.getString("wind"));

                // 整合未来天气数据
                JSONArray futureJson = resultJson.getJSONArray("future");

                List<FutureWeather> futureWeathers = new LinkedList<>();
                for (int i = 0; i < futureJson.size(); i++) {
                    FutureWeather futureWeather = new FutureWeather();
                    JSONObject tmp = (JSONObject) futureJson.get(i);

                    futureWeather.setDate(tmp.getString("date"));
                    futureWeather.setTemperature(tmp.getString("temperature"));
                    futureWeather.setWeather(tmp.getString("weather"));
                    futureWeather.setWeek(tmp.getString("week"));
                    futureWeather.setWind(tmp.getString("wind"));
                    futureWeathers.add(futureWeather);
                }

                WeatherForecastVO.setTodayWeather(todayWeather);
                WeatherForecastVO.setFutureWeathers(futureWeathers);
                return WeatherForecastVO;
            } else {
                return null;
            }
        } catch (Exception e) {
            WeatherForecastVO = null;
            e.printStackTrace();
        }

        return WeatherForecastVO;
    }

    private WeatherForecastVO findJuheWeartherByCityName(String cityName) {
        return null;
    }

    private WeatherForecastVO findSeniverseByCityName(String cityName) {
        return null;
    }

    private WeatherForecastVO findChinaWeartherByCityName(String cityName) {
        return null;
    }
}
