/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package reborncore.mixin.common;

import com.google.gson.JsonObject;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.common.crafting.ConditionManager;

import java.util.Iterator;
import java.util.Map;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {

	@Inject(method = "apply", at = @At("HEAD"))
	private void deserialize(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
		Iterator<Map.Entry<Identifier, JsonObject>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Identifier, JsonObject> entry = iterator.next();
			Identifier id = entry.getKey();
			JsonObject json = entry.getValue();

			// TODO dont hard code this as its awful
			if (id.getNamespace().equals("reborncore") || id.getNamespace().equals("techreborn")) {
				if (!ConditionManager.shouldLoadRecipe(json)) {
					iterator.remove();
				}
			}
		}
	}
}
