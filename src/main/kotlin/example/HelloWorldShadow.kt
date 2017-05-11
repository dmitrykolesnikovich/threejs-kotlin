package example

import three.Shading
import three.ShadowMap
import three.cameras.PerspectiveCamera
import three.geometries.BoxGeometry
import three.lights.PointLight
import three.materials.phong.MeshPhongMaterial
import three.materials.phong.MeshPhongMaterialParam
import three.objects.Mesh
import three.renderers.webglrenderer.WebGLRenderer
import three.renderers.webglrenderer.WebGLRendererParams
import three.scenes.Scene
import kotlin.browser.document
import kotlin.browser.window

class HelloWorldShadow {
    val renderer: WebGLRenderer
    val scene: Scene
    val camera: PerspectiveCamera
    val cube: Mesh

    init {
        scene = Scene()
        camera = PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000)
        camera.position.z = 5.0
        camera.position.y = 2.0

//        renderer = WebGLRenderer()
        renderer = WebGLRenderer(WebGLRendererParams())
        renderer.setSize(window.innerWidth, window.innerHeight)
        renderer.shadowMap.enabled = true
        renderer.shadowMap.type = ShadowMap.PCFSoftShadowMap.value
        document.body?.appendChild(renderer.domElement)

        val light = createLight()
        scene.add(light)

        cube = createCube()
        scene.add(cube)
    }

    private fun createCube(): Mesh {
        val geometry = BoxGeometry(1, 1, 1)

        val param = createPhongMaterialParam()
        val material = MeshPhongMaterial(param)

        val mesh = Mesh(geometry, material)
        mesh.castShadow = true

        return mesh
    }

    private fun createLight(): PointLight {
        val light = PointLight(0xffffff, 1, 100)
        light.position.set(0, 12, 0)
        light.castShadow = true
        light.shadow.mapSize.width = 1024
        light.shadow.mapSize.height = 1024
        return light
    }

    private fun createPhongMaterialParam(): MeshPhongMaterialParam {
        val param = MeshPhongMaterialParam()

        param.color = 0xdddddd
        param.specular = 0x999999
        param.shininess = 15
        param.shading = Shading.FlatShading.value
        return param
    }

    fun render() {
        window.requestAnimationFrame {
            cube.rotation.x += 0.01
            cube.rotation.y += 0.01
            render()
        }
        renderer.render(scene, camera)
    }
}

