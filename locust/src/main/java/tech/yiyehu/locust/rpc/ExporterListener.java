
package tech.yiyehu.locust.rpc;

import tech.yiyehu.locust.common.extension.SPI;
import tech.yiyehu.locust.rpc.exception.RpcException;

/**
 * ExporterListener. (SPI, Singleton, ThreadSafe)
 */
@SPI
public interface ExporterListener {

    /**
     * The exporter exported.
     *
     * @param exporter
     * @throws RpcException
     * @see tech.yiyehu.locust.rpc.Protocol#export(Invoker)
     */
    void exported(Exporter<?> exporter) throws RpcException;

    /**
     * The exporter unexported.
     *
     * @param exporter
     * @throws RpcException
     * @see tech.yiyehu.locust.rpc.Exporter#unexport()
     */
    void unexported(Exporter<?> exporter);

}