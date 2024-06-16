package com.codinglitch.simpleradio.client.screens;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.client.central.BaseButton;
import com.codinglitch.simpleradio.core.central.Frequencing;
import com.codinglitch.simpleradio.core.networking.packets.ServerboundRadioUpdatePacket;
import com.codinglitch.simpleradio.core.registry.menus.RadiosmitherMenu;
import com.codinglitch.simpleradio.core.central.Frequency;
import com.codinglitch.simpleradio.platform.ClientServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class RadiosmitherScreen extends AbstractContainerScreen<RadiosmitherMenu> implements ContainerListener {

    // -- Resources -- \\
    public static final ResourceLocation FM_NORMAL_SPRITE = CommonSimpleRadio.id("container/radiosmither/fm/normal");
    public static final ResourceLocation FM_SELECTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/fm/selected");
    public static final ResourceLocation FM_HIGHLIGHTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/fm/highlighted");
    public static final ResourceLocation AM_NORMAL_SPRITE = CommonSimpleRadio.id("container/radiosmither/am/normal");
    public static final ResourceLocation AM_SELECTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/am/selected");
    public static final ResourceLocation AM_HIGHLIGHTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/am/highlighted");

    public static final ResourceLocation INCREASE_NORMAL_SPRITE = CommonSimpleRadio.id("container/radiosmither/increase/normal");
    public static final ResourceLocation INCREASE_HELD_SPRITE = CommonSimpleRadio.id("container/radiosmither/increase/held");
    public static final ResourceLocation INCREASE_HIGHLIGHTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/increase/highlighted");
    public static final ResourceLocation DECREASE_NORMAL_SPRITE = CommonSimpleRadio.id("container/radiosmither/decrease/normal");
    public static final ResourceLocation DECREASE_HELD_SPRITE = CommonSimpleRadio.id("container/radiosmither/decrease/held");
    public static final ResourceLocation DECREASE_HIGHLIGHTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/decrease/highlighted");

    public static final ResourceLocation APPLY_NORMAL_SPRITE = CommonSimpleRadio.id("container/radiosmither/wrench/normal");
    public static final ResourceLocation APPLY_HIGHLIGHTED_SPRITE = CommonSimpleRadio.id("container/radiosmither/wrench/highlighted");

    private static final ResourceLocation TEXTURE = CommonSimpleRadio.id("textures/gui/container/radiosmither.png");

    private static final Component AM = Component.translatable("screen.simpleradio.radiosmither.am");
    private static final Component FM = Component.translatable("screen.simpleradio.radiosmither.fm");
    private static final Component AM_DESCRIPTION = Component.translatable("screen.simpleradio.radiosmither.am_description");
    private static final Component FM_DESCRIPTION = Component.translatable("screen.simpleradio.radiosmither.fm_description");

    // -- Constants -- \\
    private static final int INCREMENT_THRESHOLD = 10;

    // -- Fields -- \\

    public ModulationButton FM_BUTTON;
    public ModulationButton AM_BUTTON;

    public FrequencyButton INCREASE_BUTTON;
    public FrequencyButton DECREASE_BUTTON;

    public BaseButton APPLY_BUTTON;

    public EditBox FREQUENCY;

    protected String lastValidFrequency = Frequency.DEFAULT_FREQUENCY;
    public Frequency.Modulation modulation;

    protected int holdingFor = 0;
    protected int increment = 0;

    public RadiosmitherScreen(RadiosmitherMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    private void updateFrequency(String input) {
        if (!Frequency.check(input)) {
            if (!Frequency.check(lastValidFrequency)) lastValidFrequency = Frequency.DEFAULT_FREQUENCY;

            FREQUENCY.setValue(lastValidFrequency);
        } else {
            this.lastValidFrequency = input;
        }
    }

    private void reloadFrequency() {
        String frequency = this.FREQUENCY.getValue();
        if (!frequency.isEmpty()) return;

        ItemStack tinkering = menu.getTinkering();
        if (tinkering != null && tinkering.getItem() instanceof Frequencing) {
            CompoundTag tag = tinkering.getOrCreateTag();
            frequency = tag.getString("frequency");
            modulation = Frequency.modulationOf(tag.getString("modulation"));

            if (modulation != null) {
                if (modulation == Frequency.Modulation.FREQUENCY) {
                    FM_BUTTON.selected(true);
                } else if (modulation == Frequency.Modulation.AMPLITUDE) {
                    AM_BUTTON.selected(true);
                }
            }
        }
    }

    protected void incrementFrequency() { incrementFrequency(1); }
    protected void incrementFrequency(int increment) {
        String freq = this.FREQUENCY.getValue();
        if (!freq.isEmpty()) {
            this.FREQUENCY.setValue(Frequency.incrementFrequency(freq, increment));
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        reloadFrequency();

        if (increment != 0) holdingFor++;

        if (holdingFor > INCREMENT_THRESHOLD)
            incrementFrequency(increment * (1 + Math.round(holdingFor / 5f)));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mousey) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);

        this.FREQUENCY.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);

        ItemStack tinkering = this.menu.getTinkering();
        if (tinkering != null && tinkering.getItem() instanceof Frequencing) {
            if (!FREQUENCY.getValue().isEmpty() && modulation != null) {
                //graphics.drawString(this.font,
                //        Component.literal(FREQUENCY.getValue())
                //                .append(modulation == Frequency.Modulation.FREQUENCY ? FM : AM), 94, 50, Color.DARK_GRAY.getRGB(), false
                //);
            }
        }
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if (INCREASE_BUTTON.isHoveredOrFocused())
            INCREASE_BUTTON.onReleased();
        else if (DECREASE_BUTTON.isHoveredOrFocused())
            DECREASE_BUTTON.onReleased();

        return super.mouseReleased($$0, $$1, $$2);
    }

    @Override
    protected void init() {
        super.init();

        this.FREQUENCY = new EditBox(this.font, this.leftPos + 92, this.topPos + 50, 50, 12, Component.translatable("container.repair"));
        this.FREQUENCY.setTextColor(-1);
        this.FREQUENCY.setTextColorUneditable(-1);
        this.FREQUENCY.setBordered(false);
        this.FREQUENCY.setMaxLength(Frequency.FREQUENCY_DIGITS + 1);
        this.FREQUENCY.setResponder(this::updateFrequency);
        this.FREQUENCY.setValue(Frequency.DEFAULT_FREQUENCY);
        this.lastValidFrequency = Frequency.DEFAULT_FREQUENCY;
        this.addWidget(FREQUENCY);
        this.FREQUENCY.setEditable(this.menu.getSlot(0).hasItem());

        this.AM_BUTTON = new ModulationButton(this.leftPos + 89, this.topPos + 23, false, () -> {
            AM_BUTTON.selected(true);
            FM_BUTTON.selected(false);

            modulation = Frequency.Modulation.AMPLITUDE;
        });

        this.FM_BUTTON = new ModulationButton(this.leftPos + 126, this.topPos + 23, true, () -> {
            FM_BUTTON.selected(true);
            AM_BUTTON.selected(false);

            modulation = Frequency.Modulation.FREQUENCY;
        });

        this.INCREASE_BUTTON = new FrequencyButton(this.leftPos + 143, this.topPos + 45, true, this);
        this.DECREASE_BUTTON = new FrequencyButton(this.leftPos + 143, this.topPos + 54, false, this);

        this.APPLY_BUTTON = new BaseButton(
                this.leftPos + 15, this.topPos + 25,
                34, 34,
                APPLY_NORMAL_SPRITE, CommonComponents.EMPTY, () -> {
                    if (this.FREQUENCY.getValue().isEmpty() || this.modulation == null) return;
                    ClientServices.NETWORKING.sendToServer(new ServerboundRadioUpdatePacket(this.FREQUENCY.getValue(), this.modulation));
        });
        this.APPLY_BUTTON.hoverSprite = APPLY_HIGHLIGHTED_SPRITE;

        this.addRenderableWidget(AM_BUTTON);
        this.addRenderableWidget(FM_BUTTON);

        this.addRenderableWidget(INCREASE_BUTTON);
        this.addRenderableWidget(DECREASE_BUTTON);

        this.addRenderableWidget(APPLY_BUTTON);

        this.menu.addSlotListener(this);
    }

    @Override
    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }

    @Override
    public void slotChanged(AbstractContainerMenu abstractContainerMenu, int slot, ItemStack stack) {
        if (slot == 0) {
            if (!stack.isEmpty() && stack.getItem() instanceof Frequencing frequencing) {
                String frequency = frequencing.getFrequency(stack).toString();
                this.lastValidFrequency = frequency;

                this.FREQUENCY.setValue(frequency);
                this.FREQUENCY.setEditable(true);
                this.setFocused(this.FREQUENCY);
            } else {
                this.FREQUENCY.setValue("");
                this.FREQUENCY.setEditable(false);
            }
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int i1) {

    }

    public static class FrequencyButton extends BaseButton {
        private final boolean isIncrease;
        private final RadiosmitherScreen screen;

        public FrequencyButton(int x, int y, boolean isIncrease, RadiosmitherScreen screen) {
            super(x, y, 18, 9, isIncrease ? INCREASE_NORMAL_SPRITE : DECREASE_NORMAL_SPRITE, CommonComponents.EMPTY);
            this.hoverSprite = isIncrease ? INCREASE_HIGHLIGHTED_SPRITE : DECREASE_HIGHLIGHTED_SPRITE;
            this.selectedSprite = isIncrease ? INCREASE_HELD_SPRITE : DECREASE_HELD_SPRITE;

            this.screen = screen;
            this.isIncrease = isIncrease;

            this.setTooltip(Tooltip.create(isIncrease ? FM_DESCRIPTION : AM_DESCRIPTION, null));
        }

        @Override
        public void onPress() {
            this.selected = true;

            screen.increment = isIncrease ? 1 : -1;
            screen.holdingFor = 0;
            screen.incrementFrequency(screen.increment);
        }

        public void onReleased() {
            this.selected = false;
            this.setFocused(false);

            screen.increment = 0;
            screen.holdingFor = 0;
        }
    }

    public static class ModulationButton extends BaseButton {
        public ModulationButton(int x, int y, boolean isFM, Runnable onPress) {
            super(x, y, 35, 18, isFM ? FM_NORMAL_SPRITE : AM_NORMAL_SPRITE, CommonComponents.EMPTY, onPress);
            this.hoverSprite = isFM ? FM_HIGHLIGHTED_SPRITE : AM_HIGHLIGHTED_SPRITE;
            this.selectedSprite = isFM ? FM_SELECTED_SPRITE : AM_SELECTED_SPRITE;

            this.setTooltip(Tooltip.create(isFM ? FM_DESCRIPTION : AM_DESCRIPTION, null));
        }

        public void selected(boolean selected) {
            this.selected = selected;
        }
    }
}
