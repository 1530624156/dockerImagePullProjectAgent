package com.mavis.digg_agent.utils;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.exec.SearchImagesCmdExec;
import com.mavis.digg_agent.entity.vo.DockerContainerVo;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MyDockerUtil {

    //==================================== 内部方法 ====================================

    private static final long NANO_CPUS = 1000000000L;

    /**
     * 根据容器id重启容器
     * @param containerId
     * @param ip
     * @param port
     * @return
     */
    private static boolean restartDockerContainerById(String containerId,String ip,Integer port){
        AtomicBoolean f = new AtomicBoolean(false);
        execDockerCmd(ip,port,(client -> {
            try {
                client.restartContainerCmd(containerId).exec();
            } catch (NotFoundException e) {
                return;
            }
            f.set(true);
        }));
        return f.get();
    }

    /**
     * 根据ip，端口获取docker实例
     * @param ip
     * @param port
     * @return
     */
    private static DockerClient getDocker(String ip,Integer port){
        DockerClient dockerClient = DockerClientBuilder.getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(String.format("tcp://%s:%s", ip,port)).build()).build();
        if(dockerClient != null){
            return dockerClient;
        }
        return null;
    }

    /**
     * 通用执行docker方法
     * @param ip
     * @param port
     * @param callback
     */
    private static void execDockerCmd(String ip,Integer port,ExecDockerCallback callback){
        //获取客户端
        DockerClient client = getDocker(ip, port);
        if(client != null){
            callback.exec(client);
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            callback.exec(null);
        }
    }

    /**
     * 根据容器id停止容器
     * @param containerId
     * @param ip
     * @param port
     * @return
     */
    private static boolean killDockerContainerById(String containerId,String ip,Integer port){
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        execDockerCmd(ip,port,(client -> {
            try {
                client.stopContainerCmd(containerId).exec();
            } catch (Exception e) {
                return;
            }
            flag.set(true);
        }));
        return flag.get();
    }

    /**
     * 根据容器id删除容器
     * @param containerId
     * @param ip
     * @param port
     * @return
     */
    private static boolean removeDockerContainerById(String containerId,String ip,Integer port){
        AtomicBoolean f = new AtomicBoolean(false);
        execDockerCmd(ip,port,(client -> {
            try {
                client.removeContainerCmd(containerId).exec();
            } catch (Exception e) {
                return;
            }
            f.set(true);
        }));
        return f.get();
    }

    /**
     * 根据容器id启动容器
     * @param containerId
     * @param ip
     * @param port
     * @return
     */
    private static boolean startDockerContainerById(String containerId,String ip,Integer port){
        AtomicBoolean f = new AtomicBoolean(false);
        execDockerCmd(ip,port,(client -> {
            try {
                client.startContainerCmd(containerId).exec();
            } catch (Exception e) {
                return;
            }
            f.set(true);
        }));
        return f.get();
    }

    //==================================== 对外提供 ====================================

    /**
     * 根据容器名和dockerclient获取容器Vo
     * @param containerName
     * @return
     */
    public static DockerContainerVo getDockerContainerByName(String containerName,String ip,Integer port){
        if(StringUtils.isAnyEmpty(containerName,ip) || port == null){
            return null;
        }
        AtomicReference<DockerContainerVo> containerVo = new AtomicReference<>();
        execDockerCmd(ip,port,(client -> {
            //查询信息
            InspectContainerResponse info = null;
            try {
                info = client.inspectContainerCmd(containerName).exec();
            } catch (NotFoundException e) {
                return;
            }
            if(info != null){
                containerVo.set(new DockerContainerVo());
                ContainerConfig config = info.getConfig();
                //封装Vo
                containerVo.get().setId(info.getId());
                containerVo.get().setName(containerName);
                containerVo.get().setImageName(config.getImage());
                containerVo.get().setContainerAddr(info.getNetworkSettings().getIpAddress());
                Map<ExposedPort, Ports.Binding[]> bindings = info.getHostConfig().getPortBindings().getBindings();
                Set<ExposedPort> keySet = bindings.keySet();
                HashMap<Integer, Integer> ports = new HashMap<>();
                containerVo.get().setPorts(ports);
                for (ExposedPort key : keySet) {
                    Ports.Binding[] value = bindings.get(key);
                    String hostPort = value[0].getHostPortSpec();
                    int dockerPort = key.getPort();
                    ports.put(Integer.valueOf(hostPort),dockerPort);
                }
            }
        }));
        return containerVo.get();
    }

    /**
     * 根据容器名停止容器
     * @param containerName
     * @param ip
     * @param port
     * @return
     */
    public static boolean killDockerContainerByName(String containerName,String ip,Integer port){
        if(StringUtils.isAnyEmpty(containerName,ip) || port == null){
            return false;
        }
        //先查询
        DockerContainerVo containerVo = getDockerContainerByName(containerName, ip, port);
        if(containerVo != null){
            //然后根据id杀掉
            return killDockerContainerById(containerVo.getId(), ip, port);
        }
        return false;
    }

    /**
     * 根据容器名删除容器
     * @param containerName
     * @param ip
     * @param port
     * @return
     */
    public static boolean removeDockerContainerByName(String containerName,String ip,Integer port){
        if(StringUtils.isAnyEmpty(containerName,ip) || port == null){
            return false;
        }
        //先查询
        DockerContainerVo containerVo = getDockerContainerByName(containerName, ip, port);
        if(containerVo != null){
            //然后根据id杀掉
            return removeDockerContainerById(containerVo.getId(), ip, port);
        }
        return false;
    }

    /**
     * 创建容器
     * @param containerName
     * @param imageName
     * @param portBind
     * @param ip
     * @param port 宿主机：docker容器
     * @return dockerId
     */
    public static String addDockerContainerWithoutStart(String containerName, String imageName, Map<Integer,Integer> portBind, String ip, Integer port,Integer nanoCpus){
        if(StringUtils.isAnyEmpty(containerName,ip,imageName) || port == null|| portBind == null){
            return null;
        }
        //处理Docker容器的CPU限制
        if (nanoCpus == null || nanoCpus == 0){
            nanoCpus = Integer.valueOf(8);
        }
        Long finalNanoCpus = nanoCpus * NANO_CPUS;
        AtomicReference<String> id = new AtomicReference<>(null);
        execDockerCmd(ip,port,(client -> {
            CreateContainerCmd ccm = client.createContainerCmd(imageName);
            ccm.withName(containerName);
            //绑定端口
            ArrayList<PortBinding> list = new ArrayList<>();
            for (Integer key : portBind.keySet()) {
                ccm.withExposedPorts(ExposedPort.parse(portBind.get(key) + "/tcp"));
                list.add(PortBinding.parse(key + ":" + portBind.get(key)));
            }
            //创建网络配置
            HostConfig hostConfig = HostConfig.newHostConfig().withPortBindings(list);
            //创建CPU限制配置
            hostConfig.withNanoCPUs(finalNanoCpus);
            CreateContainerResponse response = ccm.withHostConfig(hostConfig).exec();
            ccm.close();
            id.set(response.getId());
        }));
        return id.get();
    }

    /**
     * 创建容器自带启动
     * @param containerName
     * @param imageName
     * @param portBind
     * @param ip
     * @param port 宿主机：docker容器
     * @return dockerId
     */
    public static String addDockerContainer(String containerName, String imageName, Map<Integer,Integer> portBind, String ip, Integer port,Integer nanoCpus){
        if(StringUtils.isAnyEmpty(containerName,ip,imageName) || port == null|| portBind == null){
            return null;
        }
        //处理Docker容器的CPU限制
        if (nanoCpus == null || nanoCpus == 0){
            nanoCpus = Integer.valueOf(8);
        }
        Long finalNanoCpus = nanoCpus * NANO_CPUS;
        AtomicReference<String> id = new AtomicReference<>(null);
        execDockerCmd(ip,port,(client -> {
            CreateContainerCmd ccm = client.createContainerCmd(imageName);
            ccm.withName(containerName);
            //绑定端口
            ArrayList<PortBinding> list = new ArrayList<>();
            for (Integer key : portBind.keySet()) {
                ccm.withExposedPorts(ExposedPort.parse(portBind.get(key) + "/tcp"));
                list.add(PortBinding.parse(key + ":" + portBind.get(key)));
            }
            //创建网络配置
            HostConfig hostConfig = HostConfig.newHostConfig().withPortBindings(list);
            //创建CPU限制配置
            hostConfig.withNanoCPUs(finalNanoCpus);
            CreateContainerResponse response = ccm.withHostConfig(hostConfig).exec();
            ccm.close();
            id.set(response.getId());
            //启动
            client.startContainerCmd(id.get()).exec();
        }));
        return id.get();
    }

    /**
     * 根据容器名重启容器
     * @param containerName
     * @param ip
     * @param port
     * @return
     */
    public static boolean restartDockerContainerByName(String containerName,String ip,Integer port){
        if(StringUtils.isAnyEmpty(containerName,ip) || port == null){
            return false;
        }
        //先查询
        DockerContainerVo containerVo = getDockerContainerByName(containerName, ip, port);
        if(containerVo != null){
            //然后根据id重启
            return restartDockerContainerById(containerVo.getId(), ip, port);
        }
        return false;
    }

    /**
     * 根据容器名启动容器
     * @param containerName
     * @param ip
     * @param port
     * @return
     */
    public static boolean startDockerContainerByName(String containerName,String ip,Integer port){
        if(StringUtils.isAnyEmpty(containerName,ip) || port == null){
            return false;
        }
        //先查询
        DockerContainerVo containerVo = getDockerContainerByName(containerName, ip, port);
        if(containerVo != null){
            //然后根据id重启
            return startDockerContainerById(containerVo.getId(), ip, port);
        }
        return false;
    }

    /**
     * 根据镜像名查询镜像
     * @param imageName 镜像名称
     * @param ip 宿主机ip
     * @param port 宿主机端口
     * @return
     */
    public static boolean searchImage(String imageName,String ip,Integer port){
        AtomicBoolean f = new AtomicBoolean(false);
        execDockerCmd(ip,port,(client -> {
            SearchImagesCmd req = client.searchImagesCmd(imageName);
            List<SearchItem> exec = req.exec();
            req.close();
            if (!exec.isEmpty()){
                f.set(true);
            }
            f.set(false);
        }));
        return f.get();
    }

    /**
     * 拉取镜像
     * @return
     */
    public static boolean pullImage(String imageName,String ip,Integer port,String registryUsername,String registryPassword){
        AtomicBoolean f = new AtomicBoolean(false);
        execDockerCmd(ip,port,(client -> {
            try {
                PullImageCmd req;
                AuthConfig authConfig = new AuthConfig();
                if (StringUtils.isNotBlank(registryUsername) && StringUtils.isNotBlank(registryPassword)){
                    authConfig.withUsername(registryUsername);
                    authConfig.withPassword(registryPassword);
                    req = client.pullImageCmd(imageName).withAuthConfig(authConfig);
                }else{
                    req = client.pullImageCmd(imageName);
                }
                PullImageResultCallback res = new PullImageResultCallback();
                req.exec(res);
                res.awaitCompletion();
            } catch (Exception e) {
                return;
            }
            f.set(true);
        }));
        return f.get();
    }
    /**
     * 删除镜像
     * @return
     */
    public static boolean removeImage(String imageName,String ip,Integer port){
        AtomicBoolean f = new AtomicBoolean(false);
        execDockerCmd(ip,port,(client -> {
            try {
                client.removeImageCmd(imageName).exec();
            } catch (Exception e) {
                return;
            }
            f.set(true);
        }));
        return f.get();
    }

    /**
     * 获取1秒的日志
     *
     * @param tail 行数
     * @param containerName
     * @param ip
     * @param port
     * @return
     */
    public static List<String> getLogByName(Integer tail, String containerName, String ip, Integer port){
        DockerContainerVo info = MyDockerUtil.getDockerContainerByName(containerName, ip, port);

        final List<String> logs = new ArrayList<>();

        MyDockerUtil.execDockerCmd(ip,port,client -> {
            LogContainerCmd cmd = client.logContainerCmd(info.getId());

            cmd.withStdOut(true).withStdErr(true);
            cmd.withTail(tail);
            try {
                cmd.exec(new LogContainerResultCallback() {
                    @Override
                    public void onNext(Frame item) {
                        logs.add(item.toString());
                    }
                }).awaitCompletion(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return logs;
    }
}

/**
 * 执行docker回调
 */
interface ExecDockerCallback{
    void exec(DockerClient client);
}
