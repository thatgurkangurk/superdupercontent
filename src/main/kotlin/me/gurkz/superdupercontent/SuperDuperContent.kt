package me.gurkz.superdupercontent

import com.tterrag.registrate.Registrate
import com.tterrag.registrate.util.nullness.NonNullSupplier
import me.gurkz.superdupercontent.java.item.ModItems
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.function.Consumer

@Mod(SuperDuperContent.MOD_ID)
object SuperDuperContent {
    const val MOD_ID = "superdupercontent"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    private val REGISTRATE = NonNullSupplier.lazy { Registrate.create(MOD_ID) }

    private val STACK_WALKER: StackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)

    @JvmStatic
    fun registrate(): Registrate {
        if (!STACK_WALKER.callerClass.packageName.startsWith("me.gurkz.superdupercontent")) {
            throw UnsupportedOperationException("please don't use superdupercontent's registrate instance.")
        }
        return REGISTRATE.get();
    }

    init {
        ModItems.register()
        MOD_BUS.addListener(::onCommonSetup)
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("hello from superdupercontent")
    }
}