/*
 * MIT License
 *
 * Copyright (c) 2024 nextCluster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.nextcluster.driver.networking.request;

import dev.httpmarco.osgan.reflections.allocator.ReflectionClassAllocater;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.nextcluster.driver.networking.packets.ByteBuffer;
import net.nextcluster.driver.networking.packets.ClusterPacket;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class RequestResponsePacket implements ClusterPacket {

    private UUID uuid;
    private ClusterPacket packet;

    @Override
    public void write(ByteBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeString(packet.getClass().getName());
        packet.write(buffer);
    }

    @Override
    public void read(ByteBuffer buffer) {
        this.uuid = buffer.readUUID();
        try {
            this.packet = (ClusterPacket) ReflectionClassAllocater.allocate(Class.forName(buffer.readString()));
            this.packet.read(buffer);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
