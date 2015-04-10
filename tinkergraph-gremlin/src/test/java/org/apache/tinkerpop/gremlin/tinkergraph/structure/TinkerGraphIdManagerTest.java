package org.apache.tinkerpop.gremlin.tinkergraph.structure;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.T;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
@RunWith(Enclosed.class)
public class TinkerGraphIdManagerTest {

    @RunWith(Parameterized.class)
    public static class NumberIdManagerTest {
        private static final Configuration longIdManagerConfig = new BaseConfiguration();
        private static final Configuration integerIdManagerConfig = new BaseConfiguration();

        @Parameterized.Parameters(name = "{0}")
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"coerceLong", 100l, 200l, 300l},
                    {"coerceInt", 100, 200, 300},
                    {"coerceDouble", 100d, 200d, 300d},
                    {"coerceFloat", 100f, 200f, 300f},
                    {"coerceString", "100", "200", "300"}});
        }

        @Parameterized.Parameter(value = 0)
        public String name;

        @Parameterized.Parameter(value = 1)
        public Object vertexIdValue;

        @Parameterized.Parameter(value = 2)
        public Object edgeIdValue;

        @Parameterized.Parameter(value = 3)
        public Object vertexPropertyIdValue;


        @BeforeClass
        public static void setup() {
            longIdManagerConfig.addProperty(TinkerGraph.CONFIG_EDGE_ID, TinkerGraph.DefaultIdManager.LONG.name());
            longIdManagerConfig.addProperty(TinkerGraph.CONFIG_VERTEX_ID, TinkerGraph.DefaultIdManager.LONG.name());
            longIdManagerConfig.addProperty(TinkerGraph.CONFIG_VERTEX_PROPERTY_ID, TinkerGraph.DefaultIdManager.LONG.name());

            integerIdManagerConfig.addProperty(TinkerGraph.CONFIG_EDGE_ID, TinkerGraph.DefaultIdManager.INTEGER.name());
            integerIdManagerConfig.addProperty(TinkerGraph.CONFIG_VERTEX_ID, TinkerGraph.DefaultIdManager.INTEGER.name());
            integerIdManagerConfig.addProperty(TinkerGraph.CONFIG_VERTEX_PROPERTY_ID, TinkerGraph.DefaultIdManager.INTEGER.name());
        }

        @Test
        public void shouldUseLongIdManagerToCoerceTypes() {
            final Graph graph = TinkerGraph.open(longIdManagerConfig);
            final Vertex v = graph.addVertex(T.id, vertexIdValue);
            final VertexProperty vp = v.property("test", "value", T.id, vertexPropertyIdValue);
            final Edge e = v.addEdge("self", v, T.id, edgeIdValue);

            assertEquals(100l, v.id());
            assertEquals(200l, e.id());
            assertEquals(300l, vp.id());
        }

        @Test
        public void shouldUseIntegerIdManagerToCoerceTypes() {
            final Graph graph = TinkerGraph.open(integerIdManagerConfig);
            final Vertex v = graph.addVertex(T.id, vertexIdValue);
            final VertexProperty vp = v.property("test", "value", T.id, vertexPropertyIdValue);
            final Edge e = v.addEdge("self", v, T.id, edgeIdValue);

            assertEquals(100, v.id());
            assertEquals(200, e.id());
            assertEquals(300, vp.id());
        }
    }


    @RunWith(Parameterized.class)
    public static class UuidIdManagerTest {
        private static final Configuration idManagerConfig = new BaseConfiguration();

        private static final UUID vertexId = UUID.fromString("0E939658-ADD2-4598-A722-2FC178E9B741");
        private static final UUID edgeId = UUID.fromString("748179AA-E319-8C36-41AE-F3576B73E05C");
        private static final UUID vertexPropertyId = UUID.fromString("EC27384C-39A0-923D-9410-271B585683B6");


        @Parameterized.Parameters(name = "{0}")
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"coerceUuid", vertexId, edgeId, vertexPropertyId},
                    {"coerceString", vertexId.toString(), edgeId.toString(), vertexPropertyId.toString()}});
        }

        @Parameterized.Parameter(value = 0)
        public String name;

        @Parameterized.Parameter(value = 1)
        public Object vertexIdValue;

        @Parameterized.Parameter(value = 2)
        public Object edgeIdValue;

        @Parameterized.Parameter(value = 3)
        public Object vertexPropertyIdValue;


        @BeforeClass
        public static void setup() {
            idManagerConfig.addProperty(TinkerGraph.CONFIG_EDGE_ID, TinkerGraph.DefaultIdManager.UUID.name());
            idManagerConfig.addProperty(TinkerGraph.CONFIG_VERTEX_ID, TinkerGraph.DefaultIdManager.UUID.name());
            idManagerConfig.addProperty(TinkerGraph.CONFIG_VERTEX_PROPERTY_ID, TinkerGraph.DefaultIdManager.UUID.name());
        }

        @Test
        public void shouldUseIdManagerToCoerceTypes() {
            final Graph graph = TinkerGraph.open(idManagerConfig);
            final Vertex v = graph.addVertex(T.id, vertexIdValue);
            final VertexProperty vp = v.property("test", "value", T.id, vertexPropertyIdValue);
            final Edge e = v.addEdge("self", v, T.id, edgeIdValue);

            assertEquals(vertexId, v.id());
            assertEquals(edgeId, e.id());
            assertEquals(vertexPropertyId, vp.id());
        }
    }
}
